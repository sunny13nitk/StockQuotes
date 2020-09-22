package root;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("root")
public class RootConfig
{
  //Nothing inside the Config Class. 
  //All beans to be loaded via Component Scan
}
