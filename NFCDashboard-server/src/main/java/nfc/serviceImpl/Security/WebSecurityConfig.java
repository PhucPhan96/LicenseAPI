package nfc.serviceImpl.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
	@Autowired
    private EntryPointUnauthorizedHandler unauthorizedHandler;
	@Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(this.userDetailsService);
                //.passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
    public JwtAuthenticationTokenFilter authenticationTokenFilterBean() throws Exception {
        return new JwtAuthenticationTokenFilter();
    }
	@Override
	public void configure(WebSecurity web) throws Exception {
		// TODO Auto-generated method stub
		//web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**");
	}
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable()
			//.headers().xssProtection().disable()
			//.headers().frameOptions().disable()
			.exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()

            // don't create session
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

            .authorizeRequests()
            .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            .antMatchers("/resources/**").permitAll()
            .antMatchers("/uploads/images/**").permitAll()
            .antMatchers("/file/upload/**").permitAll()
            
            // allow anonymous resource requests
            //.antMatchers("/file/upload/**").permitAll()
            //.antMatchers("/notify/**").permitAll()
            .antMatchers("/nfc/**").permitAll()
            .antMatchers("/customer/**").permitAll()
            //.antMatchers("/receiveGateway/**").permitAll()
            //.antMatchers("/receiveGatewayTest/**").permitAll()
            
            .antMatchers("/order/**").permitAll()
            .antMatchers("/auth/**").permitAll()
            .antMatchers("/app/**").permitAll()
            .antMatchers("/supplierImg/**").permitAll()
            .antMatchers("/sms/send").permitAll()
            .antMatchers("/category/*").permitAll()
            .antMatchers("/program/*").permitAll()
            .antMatchers("/payment/login").permitAll()
            .antMatchers("/report/**").permitAll()
                        
            .anyRequest().authenticated();

    // Custom JWT based security filter
		http
            .addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
//		/http.addFilter(authenticationTokenFilterBean());
    // disable page caching
		http.headers().cacheControl();
	}

}
