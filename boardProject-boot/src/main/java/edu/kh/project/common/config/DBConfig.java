package edu.kh.project.common.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@PropertySource("classpath:/config.properties")
public class DBConfig {
	
	//필드 
	
	@Autowired // (DI, 의존성주입)
	private ApplicationContext applicationContext ; //application scope 객체
	
		
	////////////////// HikariCp 설정 ///////////////////
	
	@Bean
	@ConfigurationProperties(prefix="spring.datasource.hikari")
	public HikariConfig hikariConfig() {
		return new HikariConfig();	
		}

   @Bean
	public DataSource dataSource(HikariConfig config) {
		DataSource dataSource = new HikariDataSource(config);
		
		return dataSource;
	}
   
   //Mybatis 설정//
   
   @Bean
   public SqlSessionFactory sessionFactory(DataSource dataSource) throws Exception {
	   
	   SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
	   
	   sessionFactoryBean.setDataSource(dataSource);
	   
 
	    
	   
	     //mapper.xml(SQL) 파일이 모이는 경로 지정
	     // -> Mybatis 코드 수행 시 mapper.xml을 읽을 수 있음
	     // sessionFactoryBean.setMapperLocations("현재프로젝트.자원.어떤파일")
	     
	   sessionFactoryBean.setMapperLocations(
			   
			    applicationContext.getResources("classpath:/mappers/**.xml")
			   );
	   
	   
	   // 해당 패키지 내 모든 클래스의 별칭을 등록 
	   // - Mybatis 는 특정 클래스 지정 시 패키지명.클래스명을 모두 작성해야함
	   // -> 너무 길다.. 긴 이름을 짧겔 부를 별칭 설정할 수 있음
	  
	   //setTypeAliasesPackage 이용시
	   // 클래스 파일명이 별칭으로 등록
	   
	   //ex) edu.kh.todo.model.dto.Todo --> Todo(별칭 등록)
	   
	   sessionFactoryBean.setTypeAliasesPackage("edu.kh.project");
	   
	   // 마이바티스 설정 파일 경로 지정 
	   
	   sessionFactoryBean.setConfigLocation(
			   applicationContext.getResource("classpath:/mybatis-config.xml")
			   
			   
			   );
	   
	   //설정 내용이 모두 적용된 객체 반환 
	   
	   return sessionFactoryBean.getObject();
	   
	   
	   
	    
   }
   
   
// SqlSessionTemplate : Connection + DBCP + Mybatis + 트랜잭션 제어 처리
	@Bean
	public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sessionFactory) {
		return new SqlSessionTemplate(sessionFactory);
	}
	// DataSourceTransactionManager : 트랜잭션 매니저
	@Bean
		public DataSourceTransactionManager dataSourceTransactionManager(DataSource dataSource) {
	return new DataSourceTransactionManager(dataSource);
	}
	   
   
   
   
   
   
   
   
   
   
   
   
   
	
}
