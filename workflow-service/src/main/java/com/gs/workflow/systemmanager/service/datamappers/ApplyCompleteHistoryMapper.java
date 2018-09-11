package com.gs.workflow.systemmanager.service.datamappers;

import com.gs.workflow.systemmanager.contract.model.crms.ApplyCompleteHistory;
import com.gs.workflow.systemmanager.service.entity.ApplyCompleteHistoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * Created by zhengyali on 2018/1/22.
 */
@Mapper(componentModel = "spring")
public interface ApplyCompleteHistoryMapper {
    /**
     * Entity to model app user info.
     *
     * @param applyCompleteHistoryEntity
     * @return
     */
    @Mappings({

    })
    ApplyCompleteHistory  entityToModel(ApplyCompleteHistoryEntity applyCompleteHistoryEntity);

    /**
     * Model to entity app user entity.
     *
     * @param applyCompleteHistory
     * @return
     */
    @Mappings({

    })
    ApplyCompleteHistoryEntity modelToEntity(ApplyCompleteHistory applyCompleteHistory);

    /**
     * Entitiesto models list.
     *
     * @param applyCompleteHistoryEntityList the app user entity
     * @return the list
     */
    List<ApplyCompleteHistory> entitiestoModels(Iterable<ApplyCompleteHistoryEntity> applyCompleteHistoryEntityList);

    /**
     * Modelsto entities list.
     *
     * @param applyCompleteHistoryList the app user entity
     * @return the list
     */
    List<ApplyCompleteHistoryEntity> modelstoEntities(List<ApplyCompleteHistory> applyCompleteHistoryList);
}
