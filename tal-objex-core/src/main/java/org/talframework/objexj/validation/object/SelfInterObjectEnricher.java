package org.talframework.objexj.validation.object;

import org.talframework.objexj.validation.groups.InterObjectEnrichmentGroup;


/**
 * Implementing this interface on an Objex object means it
 * will be called as part of the inter-object enrichment
 * step. Typically you do not use or implement this interface
 * directly, but use the Objex generators to generate out
 * the relevant code connecting to your @ObjexEnrich methods.
 *
 * @author Tom Spencer
 */
@InterObjectEnrich(groups={InterObjectEnrichmentGroup.class})
public interface SelfInterObjectEnricher {

    /**
     * Called on the object exposing this interface to enrich
     * itself with respect to its references, siblings
     * and parent.
     */
    public void enrichObjectAgainstOthers();
}
