package ch.puzzle.okr;

import ch.puzzle.okr.multitenancy.TenantConfigProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class MultitenantConfig {

    private final TenantConfigProvider tenantConfigProvider;
    private final String defaultTenantId;

    public MultitenantConfig(final TenantConfigProvider tenantConfigProvider, final @Value("${okr.default-tenant}") String defaultTenantId) {
        this.tenantConfigProvider = tenantConfigProvider;
        this.defaultTenantId = defaultTenantId;
    }
/*

    @Bean
    @Primary
    public DataSource dataSource() {
        Map<Object, Object> resolvedDataSources = new HashMap<>();

        for (TenantConfigProvider.TenantConfig tenantConfig : tenantConfigProvider.getTenantConfigs()) {
            DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
            TenantConfigProvider.DataSourceConfig dataSourceConfig = tenantConfig.dataSourceConfig();

            try {
                String tenantId = tenantConfig.tenantId();

                dataSourceBuilder
                        .type(HikariDataSource.class)
                        .driverClassName(dataSourceConfig.driverClassName())
                        .username(dataSourceConfig.name())
                        .password(dataSourceConfig.password())
                        .url(dataSourceConfig.url());

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

    @Bean
    public LocalContainerEntityManagerFactoryBean masterEntityManagerFactory(
            DataSource dataSource,
            ConfigurableListableBeanFactory beanFactory,
            JpaProperties jpaProperties,
            @Value("${okr.multitenancy.entityManager.packages}")
            String entityPackages
            ) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();

        em.setPersistenceUnitName("master-persistence-unit");
        em.setPackagesToScan(entityPackages);
        em.setDataSource(dataSource);

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        Map<String, Object> properties = new HashMap<>(jpaProperties.getProperties());
        properties.put(AvailableSettings.PHYSICAL_NAMING_STRATEGY, "org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy");
        properties.put(AvailableSettings.IMPLICIT_NAMING_STRATEGY, "org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy");
        properties.put(AvailableSettings.BEAN_CONTAINER, new SpringBeanContainer(beanFactory));
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Bean
    public JpaTransactionManager masterTransactionManager(
            @Qualifier("masterEntityManagerFactory") EntityManagerFactory emf) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);
        return transactionManager;
    }


    @Primary
    @Bean
    public LocalContainerEntityManagerFactoryBean tenantEntityManagerFactory(
            SchemaBasedMultiTenantConnectionProvider connectionProvider,
            CurrentTenantIdentifierResolverImpl tenantResolver,
            ConfigurableListableBeanFactory beanFactory,
            JpaProperties jpaProperties,
            @Value("${okr.multitenancy.entityManager.packages}")
            String entityPackages) {
        LocalContainerEntityManagerFactoryBean emfBean = new LocalContainerEntityManagerFactoryBean();
        emfBean.setPersistenceUnitName("tenant-persistence-unit");
        emfBean.setPackagesToScan(entityPackages);

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        emfBean.setJpaVendorAdapter(vendorAdapter);

        Map<String, Object> properties = new HashMap<>(jpaProperties.getProperties());
        // properties.put(AvailableSettings.PHYSICAL_NAMING_STRATEGY, "org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy");
        // properties.put(AvailableSettings.IMPLICIT_NAMING_STRATEGY, "org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy");
        properties.put(AvailableSettings.BEAN_CONTAINER, new SpringBeanContainer(beanFactory));
        properties.remove(AvailableSettings.DEFAULT_SCHEMA);
        properties.put(AvailableSettings.MULTI_TENANT_CONNECTION_PROVIDER, connectionProvider);
        properties.put(AvailableSettings.MULTI_TENANT_IDENTIFIER_RESOLVER, tenantResolver);
        emfBean.setJpaPropertyMap(properties);

        return emfBean;
    }

    @Primary
    @Bean
    public JpaTransactionManager tenantTransactionManager(
            @Qualifier("tenantEntityManagerFactory") EntityManagerFactory emf) {
        JpaTransactionManager tenantTransactionManager = new JpaTransactionManager();
        tenantTransactionManager.setEntityManagerFactory(emf);
        return tenantTransactionManager;
    }
 */

}