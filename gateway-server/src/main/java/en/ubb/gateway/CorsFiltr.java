package en.ubb.gateway;


import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CorsFiltr implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        final HttpServletResponse response = (HttpServletResponse) res;
		        System.out.println("CORS FILTER WORKING IN GATEWAY:::");

		        response.setHeader("Access-Control-Allow-Origin", "*");
		        response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE,OPTIONS");
		        response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type,x-requested-with");
		        response.setHeader("Access-Control-Max-Age", "3600");
        if ("OPTIONS".equalsIgnoreCase(((HttpServletRequest) req).getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            chain.doFilter(req, res);
        }
    }

    @Override
    public void destroy() {

    }
}
