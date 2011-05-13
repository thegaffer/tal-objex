/**
 * This package contains the annotations which are intended to mark
 * intent in your domain objects and their properties.
 * 
 * <p>It should be noted that the intent here is not to specify
 * either validation or persistence concerns. Validation should be
 * specified inside your objects with your own code, or other
 * annotations/aspects specifically for validation. Persistence
 * is the concern of some persistence engine. If your using a
 * persistence engine that needs information include that in
 * separate annotations (if you have to!).
 * 
 * <p>These annotations are used by Objex to deal with the objects,
 * but every attempt is made in the annotations to make them free
 * of Objex (save for the package!), so feel free to use them in
 * other situations.</p>
 */
package org.talframework.domain.annotations;