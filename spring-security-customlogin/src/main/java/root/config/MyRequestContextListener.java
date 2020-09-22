package root.config;

import javax.servlet.annotation.WebListener;

import org.springframework.web.context.request.RequestContextListener;

// To Use Session Scoped Beans
@WebListener
public class MyRequestContextListener extends RequestContextListener
{
}