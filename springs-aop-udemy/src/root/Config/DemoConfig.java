package root.Config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration // Enabling Pure JAVA Config for Spring App
@EnableAspectJAutoProxy // Enabling AOP AutoPoxy - Generate Proxies for Adviced Beans
@ComponentScan(
    // REcursively SCan for Beans and Aspects in Path
"root"
)
public class DemoConfig
{
	
}
