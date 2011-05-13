/*
/*
 * Copyright 2009 Thomas Spencer
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.talframework.objexj.container.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.ValidationError;
import org.talframework.objexj.ValidationRequest;
import org.talframework.objexj.ValidationRequest.ValidationType;
import org.talframework.objexj.container.ContainerObjectCache;
import org.talframework.objexj.container.ContainerObjectCache.CacheState;
import org.talframework.objexj.validation.CurrentValidationRequest;
import org.talframework.objexj.validation.SimpleValidationRequest;

/**
 * This class validates a container. It does this currently in
 * 3 steps:
 * 
 * <ul>
 * <li>Step 1 is to validate all new/changed objects that they 
 * are valid in themselves</li>
 * <li>Step 2 is to validate all new/changed objects that they
 * are valid with respect to collaborators (other objects)</li>
 * <li>Step 3 is to validate all parents of new/changed objects
 * that their children are ok. The root object of the container
 * is always validated here against even if it or any of its 
 * direct children are not new/changed.</li>
 * 
 * The request object is then returned which holds all the errors.
 * Typically this is then passed to the root object to filtration
 * as some errors might be downgraded to warnings.</li>
 * 
 * @author Tom Spencer
 */
public final class ContainerValidator {

    /** Holds the transaction cache of all objects to validate */
    private final ContainerObjectCache cache;
    
    /**
     * Static method to enable 1-line validation of a container
     * and it's objects.
     * 
     * @param cache The cache for the transaction
     * @return The request (which contains any errors)
     */
    public final static ValidationRequest validate(final ContainerObjectCache cache, final ValidationRequest oldRequest) {
        ContainerValidator validator = new ContainerValidator(cache);
        return validator.validate(oldRequest);
    }
    
    public ContainerValidator(final ContainerObjectCache cache) {
        this.cache = cache;
    }
    
    public final ValidationRequest validate(ValidationRequest old) {
        SimpleValidationRequest request = new SimpleValidationRequest(ValidationType.INTRA_OBJECT);
        
        Map<ObjexID, List<ValidationError>> oldErrors = old != null ? old.getErrorMap() : null;
        
        ValidationRequest previous = null;
        try {
            previous = CurrentValidationRequest.start(request);
            
            // Step 1 - Validate all (active) objects within themselves
            Set<ObjexObj> changedObjects = cache.getObjects(CacheState.NEWORCHANGED);
            for( ObjexObj obj : changedObjects ) obj.validate(request);
            
            // Step 2 - Validate all objects against others
            request.setType(ValidationType.INTER_OBJECT); 
            for( ObjexObj obj : changedObjects ) obj.validate(request);
            
            // Step 3 - Validate the parents all the way to the root
            request.setType(ValidationType.CHILDREN);
            ParentObjectBuilder parents = getParentObjectBuilder(changedObjects);
            for( int i = parents.getMaxDepth(); i >= 0 ; i-- ) {
                Iterator<ObjexObj> it2 = parents.getObjects(i).iterator();
                while( it2.hasNext() ) {
                    ObjexObj parent = it2.next();
                    if( oldErrors != null && oldErrors.containsKey(parent.getId()) ) oldErrors.remove(parent.getId());
                    parent.validate(request);
                }
            }
        }
        finally {
            CurrentValidationRequest.end(previous);
        }
        
        // Put back any errors on objects not in transaction
        if( oldErrors != null && oldErrors.size() > 0 ) {
            Iterator<ObjexID> it = oldErrors.keySet().iterator();
            while( it.hasNext() ) {
                ObjexID id = it.next();
                
                CacheState state = cache.getObjectCacheState(id);
                if( state == null || state == CacheState.NONE || state == CacheState.ACTIVE ) {
                    request.addErrors(id, oldErrors.get(id));
                }
            }
        }
        
        return request;
    }
    
    /**
     * Helper to get the parent object builder for all parent
     * objects.
     * 
     * @return The parent object builder.
     */
    private final ParentObjectBuilder getParentObjectBuilder(final Set<ObjexObj> changedObjects) {
        ParentObjectBuilder builder = new ParentObjectBuilder();
        
        for( ObjexObj obj : changedObjects ) builder.addParent(obj);
        
        return builder;
    }
    
    /**
     * This private inner class contains the magic for arranging
     * the parent objects by depth, where the root object i depth 0
     * and any of its children are depth 1 etc, etc. It also
     * contains logic to ensure a parent object is only evaluated
     * once and then provides methods to get the max depth and
     * all parent objects at that depth.
     *
     * @author Tom Spencer
     */
    private final static class ParentObjectBuilder {
        /** Holds an array of parent objects at a given depth */
        private List<List<ObjexObj>> parentObjects = new ArrayList<List<ObjexObj>>();
        /** Holds a temp map to ensure we don't re-add the same object twice */
        private Map<ObjexID, Integer> parents = new HashMap<ObjexID, Integer>();
        
        /**
         * Ensures the parent of the given object is
         * added to the set of parentObjects for later
         * validation.
         * 
         * @param obj The object whose parent we want to add
         * @return The depth of obj if it is a parent
         */
        public final int addParent(final ObjexObj obj) {
            ObjexObj parent = obj.getParent();
            
            if( parent == null || parent == obj || parent.getId().equals(obj.getId()) ) {
                return 0;
            }
            else if( !parents.containsKey(parent.getId()) ) {
                int val = addParent(parent);
                
                // Ensure we don't re-add this again
                parents.put(parent.getId(), val);
                
                // Add it as a parent
                while( parentObjects.size() <= val ) parentObjects.add(null);
                if( parentObjects.get(val) == null ) parentObjects.set(val, new ArrayList<ObjexObj>());
                parentObjects.get(val).add(parent);

                return val + 1;
            }
            else {
                return parents.get(parent.getId());
            }
        }
        
        public final int getMaxDepth() {
            return parentObjects.size() - 1;
        }
        
        public final List<ObjexObj> getObjects(final int depth) {
            List<ObjexObj> ret = parentObjects.get(depth);
            if( ret == null ) ret = new ArrayList<ObjexObj>();
            return ret;
        }
    }
}
