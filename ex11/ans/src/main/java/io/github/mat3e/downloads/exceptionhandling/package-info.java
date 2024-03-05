/**
 * Base business exceptions handling.
 * <p>
 * All modules can throw {@link io.github.mat3e.downloads.exceptionhandling.BusinessException BusinessException} or its
 * subclasses to emphasize business conditions were not followed (especially
 * {@link io.github.mat3e.downloads.exceptionhandling.EntityNotFoundException EntityNotFoundException} can signal a
 * missing resource) and they will be handled gracefully when exposed via REST API.
 */
package io.github.mat3e.downloads.exceptionhandling;
