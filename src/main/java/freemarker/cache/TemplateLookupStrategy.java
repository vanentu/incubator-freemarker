package freemarker.cache;

import java.io.IOException;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * Finds the {@link TemplateLoader}-level (storage-level) template source for a template name. This usually means trying
 * various {@link TemplateLoader}-level template names that were deduced from the requested name. Trying a name usually
 * means calling {@link TemplateLookupContext#lookupWithAcquisitionStrategy(String)} with it and checking the value of
 * {@link TemplateLookupResult#isPositive()}.
 * 
 * <p>
 * Before you write your own lookup strategy, know that:
 * <ul>
 * <li>A template lookup strategy meant to operate solely with template names, not with {@link TemplateLoader}-s
 * directly. Basically, it's a mapping between the template names that templates and API-s like
 * {@link Configuration#getTemplate(String)} see, and those that the underlying {@link TemplateLoader} sees.
 * <li>A template lookup strategy doesn't influence the template's name ({@link Template#getName()}), which is the
 * normalized form of the template name as it was requested (with {@link Configuration#getTemplate(String)}, etc.). It
 * only influences the so called source name of the template ({@link Template#getSourceName()}). The template's name is
 * used as the basis for resolving relative inclusions/imports in the template. The source name is pretty much only used
 * in error messages as error location.
 * <li>Understand the impact of the last point if your template lookup strategy fiddles not only with the file name part
 * of the template name, but also with the directory part. For example, one may want to map "foo.ftl" to "en/foo.ftl",
 * "fr/foo.ftl", etc. That's legal, but the result is kind of like if you had several root directories ("en/", "fr/",
 * etc.) that are layered over each other to form a single merged directory. (This is what's desirable in typical
 * applications, yet it can be confusing.)
 * </ul>
 * 
 * @see Configuration#setTemplateLookupStrategy(TemplateLookupStrategy)
 * 
 * @since 2.3.22
 */
public interface TemplateLookupStrategy {

    /**
     * Finds the template source that matches the template name, locale (if not {@code null}) and other parameters
     * specified in the {@link TemplateLookupContext}. See also the class-level {@link TemplateLookupStrategy}
     * documentation to understand lookup strategies more.
     * 
     * @param ctx
     *            Contains the parameters for which the matching template need to be found, and operations that
     *            are needed to implement the strategy. Some of the important input parameters are:
     *            {@link TemplateLookupContext#getTemplateName()}, {@link TemplateLookupContext#getTemplateLocale()}.
     *            The most important operations are {@link TemplateLookupContext#lookupWithAcquisitionStrategy(String)}
     *            and {@link TemplateLookupContext#createNegativeLookupResult()}.
     * 
     * @return Usually the return value of {@link TemplateLookupContext#lookupWithAcquisitionStrategy(String)}, or
     *         {@code TemplateLookupContext#createNegativeLookupResult()} if no matching template exists. Can't be
     *         {@code null}.
     */
    TemplateLookupResult lookup(TemplateLookupContext ctx) throws IOException;

}