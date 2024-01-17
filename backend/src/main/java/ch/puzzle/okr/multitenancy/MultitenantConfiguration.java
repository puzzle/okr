package ch.puzzle.okr.multitenancy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class MultitenantConfiguration {

    private final TenantConfigProvider tenantConfigProvider;
    private final String defaultTenantId;

    public MultitenantConfiguration(final TenantConfigProvider tenantConfigProvider, final @Value("${okr.default-tenant}") String defaultTenantId) {
        this.tenantConfigProvider = tenantConfigProvider;
        this.defaultTenantId = defaultTenantId;
    }


    @Bean
    public DataSource dataSource() {
        Map<Object, Object> resolvedDataSources = new HashMap<>();

        for (TenantConfigProvider.TenantConfig tenantConfig : tenantConfigProvider.getTenantConfigs()) {
            DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
            TenantConfigProvider.DataSourceConfig dataSourceConfig = tenantConfig.dataSourceConfig();

            try {
                String tenantId = tenantConfig.tenantId();

                dataSourceBuilder.driverClassName(dataSourceConfig.driverClassName());
                dataSourceBuilder.username(dataSourceConfig.name());
                dataSourceBuilder.password(dataSourceConfig.password());
                dataSourceBuilder.url(dataSourceConfig.url());
                resolvedDataSources.put(tenantId, dataSourceBuilder.build());
            } catch (Exception exp) {
                throw new RuntimeException(MessageFormat.format("Problem in tenant ({0}) datasource:{1}", tenantConfig.tenantId(), exp));
            }
        }

        AbstractRoutingDataSource dataSource = new MultitenantDataSource();
        dataSource.setDefaultTargetDataSource(resolvedDataSources.get(this.defaultTenantId));
        dataSource.setTargetDataSources(resolvedDataSources);

        dataSource.afterPropertiesSet();
        return dataSource;
    }

}