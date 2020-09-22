package modelframework.implementations;

import org.springframework.stereotype.Service;

import modelframework.annotations.FormatMessage;
import modelframework.interfaces.IPropertyAwareMessage;

@Service
public class MessagesFormatter
	{
		private IPropertyAwareMessage message_snippet;
		
		public IPropertyAwareMessage getMessage_snippet()
			{
				return message_snippet;
			}
			
		public void setMessage_snippet(IPropertyAwareMessage message_snippet)
			{
				this.message_snippet = message_snippet;
			}
			
		@FormatMessage
		public String generate_formatted_message()
			{
				return null;
			}
			
		@FormatMessage
		public String generate_message_snippet(IPropertyAwareMessage message_snippet)
			{
				setMessage_snippet(message_snippet);
				return generate_formatted_message();
			}
			
	}
