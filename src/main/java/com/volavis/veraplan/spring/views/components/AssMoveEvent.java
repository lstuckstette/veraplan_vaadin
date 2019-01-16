package com.volavis.veraplan.spring.views.components;

import com.helger.commons.collection.map.MapEntry;
import com.volavis.veraplan.spring.planimport.model.ImportTeacher;
import com.volavis.veraplan.spring.planimport.model.ImportTimeslot;
import com.volavis.veraplan.spring.views.PlanCollaborationView;
import org.apache.commons.collections4.keyvalue.MultiKey;
import org.apache.commons.collections4.map.MultiKeyMap;

import java.util.List;
import java.util.Map;

public class AssMoveEvent {

    private long eventSourceUserId;

//    private Map<MultiKey<? extends Integer>, List<PlanCollaborationView.ModelEntry>> model;

    private MultiKeyMap<Integer, List<PlanCollaborationView.ModelEntry>> model;

    public AssMoveEvent() {

    }

    public long getEventSourceUserId() {
        return eventSourceUserId;
    }

    public void setEventSourceUserId(long eventSourceUserId) {
        this.eventSourceUserId = eventSourceUserId;
    }

    public MultiKeyMap<Integer, List<PlanCollaborationView.ModelEntry>> getModel() {
        return model;
    }

    public void setModel(MultiKeyMap<Integer, List<PlanCollaborationView.ModelEntry>> model) {
        this.model = model;
    }


//    private ImportTimeslot sourceSlot;
//    private int sourceId;
//
//    private ImportTimeslot targetSlot;
//    private int targetId;
//
//    public ImportTimeslot getSourceSlot() {
//        return sourceSlot;
//    }
//
//    public void setSourceSlot(ImportTimeslot sourceSlot) {
//        this.sourceSlot = sourceSlot;
//    }
//
//    public long getEventSourceUserId() {
//        return eventSourceUserId;
//    }
//
//    public void setEventSourceUserId(long eventSourceUserId) {
//        this.eventSourceUserId = eventSourceUserId;
//    }
//
//    public int getSourceId() {
//        return sourceId;
//    }
//
//    public void setSourceId(int sourceId) {
//        this.sourceId = sourceId;
//    }
//
//    public int getTargetId() {
//        return targetId;
//    }
//
//    public void setTargetId(int targetId) {
//        this.targetId = targetId;
//    }
//
//    public ImportTimeslot getTargetSlot() {
//        return targetSlot;
//    }
//
//    public void setTargetSlot(ImportTimeslot targetSlot) {
//        this.targetSlot = targetSlot;
//    }

}