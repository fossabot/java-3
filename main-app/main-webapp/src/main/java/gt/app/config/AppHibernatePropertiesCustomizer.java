package gt.app.config;

import gt.app.config.logging.HibernateStatInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;

import java.util.Map;

//@Component
@RequiredArgsConstructor
//@Profile("!test")
class AppHibernatePropertiesCustomizer implements HibernatePropertiesCustomizer {

    private final HibernateStatInterceptor statInterceptor;

    @Override
    public void customize(Map<String, Object> props) {

        props.put("hibernate.session_factory.interceptor", statInterceptor);
    }
}
