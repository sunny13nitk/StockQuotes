package root.validation.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(
    { ElementType.ANNOTATION_TYPE, ElementType.TYPE }
)
@Retention(
    RetentionPolicy.RUNTIME
)
public @interface FieldMatchList
{
	FieldMatch[] value();
}
