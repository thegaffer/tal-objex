package org.talframework.objexj.validation.object;

import org.talframework.objexj.validation.groups.IntraObjectEnrichmentGroup;


/**
 * Implementing this interface on an Objex object means it
 * will be called as part of the intra-object enrichment
 * step. Typically you do not use or implement this interface
 * directly, but use the Objex generators to generate out
 * the relevant code connecting to your @ObjexEnrich methods.
 *
 * @author Tom Spencer
 */
@IntraObjectEnrich(groups={IntraObjectEnrichmentGroup.class})
public interface SelfIntraObjectEnricher {

    /**
     * Called on the object exposing this interface to actually
     * enrich itself with respect to its internal state.
     */
    public void enrichObject();
}
