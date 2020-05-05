package ua.nure.cherkashyn.hotel.web.filter;

import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;


/**
 * LocaleFilter
 * <p>
 * this filter need for setDefault locale
 *
 * @author Vladimir Cherkashyn
 */
public class LocaleFilter implements Filter {

    private static final Logger LOG = Logger.getLogger(LocaleFilter.class);

    private String defaultLocale;

    public void destroy() {
        LOG.debug("Filter destruction starts");
        // no op
        LOG.debug("Filter destruction finished");
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        LOG.debug("Filter starts");
        HttpServletRequest httpRequest = (HttpServletRequest) req;

        HttpSession session = httpRequest.getSession();

        if (session.getAttribute("locale") == null) {
            session.setAttribute("locale", defaultLocale);
        }

        LOG.debug("Filter finished");
        chain.doFilter(req, resp);
    }

    public void init(FilterConfig fConfig) {
        LOG.debug("Filter initialization starts");
        defaultLocale = fConfig.getInitParameter("defaultLocale");
        LOG.trace("DefaultLocale from web.xml --> " + defaultLocale);
        LOG.debug("Filter initialization finished");
    }

}
