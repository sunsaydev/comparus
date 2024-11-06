package ua.comparus.useraggregation.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Service;
import ua.comparus.useraggregation.config.DataSourceProperties;
import ua.comparus.useraggregation.dtos.UserDTO;
import ua.comparus.useraggregation.entities.DynamicUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class UserAggregationService {

    @Autowired
    private DataSourceProperties dataSourceProperties;

    @Autowired
    @Qualifier("entityManagerFactories")
    private Map<String, LocalContainerEntityManagerFactoryBean> entityManagerFactories;

    public List<UserDTO> getAllUsers() {
        List<UserDTO> aggregatedUsersDTO = new ArrayList<>();

        for (DataSourceProperties.DataSourceConfig dataSourceConfig : dataSourceProperties.getDataSources()) {
            LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = entityManagerFactories.get(dataSourceConfig.getName());
            if (entityManagerFactoryBean == null) continue;
            EntityManagerFactory entityManagerFactory = entityManagerFactoryBean.getObject();
            if (entityManagerFactory == null) continue;
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            String tableName = dataSourceConfig.getTable();
            String id = dataSourceConfig.getMapping().getId();
            String username = dataSourceConfig.getMapping().getUsername();
            String name = dataSourceConfig.getMapping().getName();
            String surname = dataSourceConfig.getMapping().getSurname();
            String queryStr = "SELECT " + id + ", " + username + ", " + name
                    + ", " + surname + " FROM " + tableName;
            List<Object[]> resultList = entityManager.createNativeQuery(queryStr).getResultList();
            List<DynamicUser> users = new ArrayList<>();
            for (Object[] row : resultList) {
                String rsid = (String) row[0];
                String rsusername = (String) row[1];
                String rsname = (String) row[2];
                String rssurname = (String) row[3];
                DynamicUser user = new DynamicUser(rsid, rsusername, rsname, rssurname);
                users.add(user);
            }
            for (DynamicUser userEntity : users) {
                UserDTO userDTO = mapToDTO(userEntity);
                aggregatedUsersDTO.add(userDTO);
            }
        }

        return aggregatedUsersDTO;
    }

    private UserDTO mapToDTO(DynamicUser userEntity) {
        UserDTO dto = new UserDTO();
        dto.setId(userEntity.getId());
        dto.setUsername(userEntity.getUsername());
        dto.setName(userEntity.getName());
        dto.setSurname(userEntity.getSurname());
        return dto;
    }
}
