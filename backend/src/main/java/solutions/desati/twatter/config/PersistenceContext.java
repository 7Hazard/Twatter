package solutions.desati.twatter.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = {
        "solutions.desati.twatter.repositories"
})
@EnableTransactionManagement
class PersistenceContext {

}