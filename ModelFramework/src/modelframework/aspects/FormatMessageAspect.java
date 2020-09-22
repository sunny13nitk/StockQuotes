package modelframework.aspects;

import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import modelframework.exposed.FrameworkManager;
import modelframework.implementations.MessagesFormatter;
import modelframework.interfaces.IPropertyAwareMessage;

@Component
@Aspect
public class FormatMessageAspect
	{
		@Autowired
		private FrameworkManager	frameworkManager;
		@Autowired
		private MessageSource		messageSource;
		
		/**
		 * 
		 * @param proceedingJoinPoint
		 * @return
		 */
		@Around("@annotation(modelframework.annotations.FormatMessage)")
		public String FormatMessageAdvice(ProceedingJoinPoint proceedingJoinPoint)
			{
				String formatted_message = null;
				// Before
				if (messageSource != null && (proceedingJoinPoint.getTarget() instanceof MessagesFormatter))
					{
						try
							{
								// Get the handle to return value
								formatted_message = (String) proceedingJoinPoint.proceed();
								
								MessagesFormatter msg_formatter = (MessagesFormatter) proceedingJoinPoint.getTarget();
								if (msg_formatter != null)
									{
										IPropertyAwareMessage prop_aware_msg = msg_formatter.getMessage_snippet();
										if (prop_aware_msg != null)
											{
												formatted_message = messageSource.getMessage(prop_aware_msg.getProperty_ID(), prop_aware_msg.getArguments(), null,
												      Locale.ENGLISH);
												
												// also log the messages to Framework Manager Log
												if (formatted_message != null)
													{
														if (frameworkManager != null)
															{
																@SuppressWarnings("static-access")
																Logger logger = frameworkManager.getLogger();
																if (logger != null)
																	{
																		if (prop_aware_msg.IS_Exception() == true)
																			{
																				logger.log(Level.SEVERE, formatted_message);
																			}
																		else
																			{
																				logger.log(Level.INFO, formatted_message);
																			}
																	}
															}
													}
													
											}
									}
									
							}
						catch (Throwable e)
							{
								if (frameworkManager != null)
									{
										@SuppressWarnings("static-access")
										Logger logger = frameworkManager.getLogger();
										if (logger != null)
											{
												MessagesFormatter msg_formatter = (MessagesFormatter) proceedingJoinPoint.getTarget();
												IPropertyAwareMessage prop_aware_msg = msg_formatter.getMessage_snippet();
												if (prop_aware_msg != null)
													{
														String msg = messageSource.getMessage("ERR_MSG_FORMATTING", new Object[]
															{ prop_aware_msg.getProperty_ID(), }, Locale.ENGLISH) + "- " + e.getMessage();
														logger.log(Level.SEVERE, msg);
													}
											}
									}
							}
							
					}
				return formatted_message;
			}
			
	}