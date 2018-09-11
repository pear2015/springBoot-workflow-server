package com.gs.workflow.systemmanager.service.datamappers;

import com.gs.workflow.systemmanager.contract.model.crms.WaitApportion;
import com.gs.workflow.systemmanager.service.entity.WaitApportionEntity;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.1.0.Final, compiler: javac, environment: Java 1.8.0_131 (Oracle Corporation)"
)
@Component
public class WaitApportionMapperImpl implements WaitApportionMapper {

    @Override
    public WaitApportion entityToModel(WaitApportionEntity waitApportionEntity) {
        if ( waitApportionEntity == null ) {
            return null;
        }

        WaitApportion waitApportion = new WaitApportion();

        waitApportion.setApplyInfoId( waitApportionEntity.getApplyInfoId() );
        waitApportion.setApplyPriority( waitApportionEntity.getApplyPriority() );
        waitApportion.setCreateTime( waitApportionEntity.getCreateTime() );
        waitApportion.setWaitReasonType( waitApportionEntity.getWaitReasonType() );
        waitApportion.setProcessId( waitApportionEntity.getProcessId() );
        waitApportion.setBusinessType( waitApportionEntity.getBusinessType() );
        waitApportion.setApportionId( waitApportionEntity.getApportionId() );

        return waitApportion;
    }

    @Override
    public WaitApportionEntity modelToEntity(WaitApportion waitApportion) {
        if ( waitApportion == null ) {
            return null;
        }

        WaitApportionEntity waitApportionEntity = new WaitApportionEntity();

        waitApportionEntity.setApportionId( waitApportion.getApportionId() );
        waitApportionEntity.setApplyInfoId( waitApportion.getApplyInfoId() );
        waitApportionEntity.setCreateTime( waitApportion.getCreateTime() );
        waitApportionEntity.setApplyPriority( waitApportion.getApplyPriority() );
        waitApportionEntity.setWaitReasonType( waitApportion.getWaitReasonType() );
        waitApportionEntity.setProcessId( waitApportion.getProcessId() );
        waitApportionEntity.setBusinessType( waitApportion.getBusinessType() );

        return waitApportionEntity;
    }

    @Override
    public List<WaitApportion> entitiestoModels(Iterable<WaitApportionEntity> waitApportionEntityList) {
        if ( waitApportionEntityList == null ) {
            return null;
        }

        List<WaitApportion> list = new ArrayList<WaitApportion>();
        for ( WaitApportionEntity waitApportionEntity : waitApportionEntityList ) {
            list.add( entityToModel( waitApportionEntity ) );
        }

        return list;
    }

    @Override
    public List<WaitApportionEntity> modelstoEntities(List<WaitApportion> waitApportionList) {
        if ( waitApportionList == null ) {
            return null;
        }

        List<WaitApportionEntity> list = new ArrayList<WaitApportionEntity>();
        for ( WaitApportion waitApportion : waitApportionList ) {
            list.add( modelToEntity( waitApportion ) );
        }

        return list;
    }
}
