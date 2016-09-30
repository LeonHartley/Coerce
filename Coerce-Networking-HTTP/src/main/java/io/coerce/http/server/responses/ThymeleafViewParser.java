package io.coerce.http.server.responses;

import io.coerce.networking.http.responses.views.ViewParser;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.resourceresolver.ClassLoaderResourceResolver;
import org.thymeleaf.resourceresolver.FileResourceResolver;
import org.thymeleaf.templateresolver.TemplateResolver;

import java.util.Map;

/**
 * @Author Per Wendel
 * https://github.com/perwendel/spark-template-engines/blob/master/spark-template-thymeleaf/
 */
public class ThymeleafViewParser implements ViewParser {

    private static final String DEFAULT_PREFIX = "templates/";
    private static final String DEFAULT_SUFFIX = ".html";
    private static final String DEFAULT_TEMPLATE_MODE = "XHTML";
    private static final long DEFAULT_CACHE_TTL_MS = 3600000L;

    private TemplateEngine templateEngine;

    public ThymeleafViewParser() {
        this.initialize(createDefaultTemplateResolver(DEFAULT_PREFIX, DEFAULT_SUFFIX));
    }

    private void initialize(TemplateResolver templateResolver) {
        templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
    }

    private static TemplateResolver createDefaultTemplateResolver(String prefix, String suffix) {
        TemplateResolver defaultTemplateResolver = new TemplateResolver();
        defaultTemplateResolver.setTemplateMode(DEFAULT_TEMPLATE_MODE);

        defaultTemplateResolver.setPrefix(
                prefix != null ? prefix : DEFAULT_PREFIX
        );

        defaultTemplateResolver.setSuffix(
                suffix != null ? suffix : DEFAULT_SUFFIX
        );

        //defaultTemplateResolver.setCacheable(false);
        defaultTemplateResolver.setCacheTTLMs(DEFAULT_CACHE_TTL_MS);
        defaultTemplateResolver.setResourceResolver(new FileResourceResolver());
        return defaultTemplateResolver;
    }

    @Override
    public String render(String view, Map<String, Object> model) {
        Context context = new Context();
        context.setVariables(model);

        return templateEngine.process(view, context);
    }
}
