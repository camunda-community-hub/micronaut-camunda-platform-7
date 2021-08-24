/*
 * Copyright 2021 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package info.novatec.micronaut.camunda.bpm.feature.eventing;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;

/**
 * @author Silvan Brenner
 */
// Implementation based on https://github.com/camunda/camunda-bpm-platform/blob/master/spring-boot-starter/starter/src/main/java/org/camunda/bpm/spring/boot/starter/event/ExecutionEvent.java
public class ExecutionEvent {

    protected final String activityInstanceId;
    protected final String businessKey;
    protected final String currentActivityId;
    protected final String currentActivityName;
    protected final String currentTransitionId;
    protected final String eventName;
    protected final String id;
    protected final String parentActivityInstanceId;
    protected final String parentId;
    protected final String processBusinessKey;
    protected final String processDefinitionId;
    protected final String processInstanceId;
    protected final String tenantId;

    public ExecutionEvent(DelegateExecution delegateExecution) {
        this.activityInstanceId = delegateExecution.getActivityInstanceId();
        this.businessKey = delegateExecution.getBusinessKey();
        this.currentActivityId = delegateExecution.getCurrentActivityId();
        this.currentActivityName = delegateExecution.getCurrentActivityName();
        this.currentTransitionId = delegateExecution.getCurrentTransitionId();
        this.eventName = delegateExecution.getEventName();
        this.id = delegateExecution.getId();
        this.parentActivityInstanceId = delegateExecution.getParentActivityInstanceId();
        this.parentId = delegateExecution.getParentId();
        this.processBusinessKey = delegateExecution.getProcessBusinessKey();
        this.processDefinitionId = delegateExecution.getProcessDefinitionId();
        this.processInstanceId = delegateExecution.getProcessInstanceId();
        this.tenantId = delegateExecution.getTenantId();
    }

    /**
     * @return the Id of the activity instance currently executed by this execution
     */
    public String getActivityInstanceId() {
        return activityInstanceId;
    }

    /**
     * @return the business key for the root execution (e.g. process instance).
     */
    public String getBusinessKey() {
        return businessKey;
    }

    /**
     * @return the id of the current activity.
     */
    public String getCurrentActivityId() {
        return currentActivityId;
    }

    /**
     * @return the name of the current activity.
     */
    public String getCurrentActivityName() {
        return currentActivityName;
    }

    /**
     * @return the Id of the current transition
     */
    public String getCurrentTransitionId() {
        return currentTransitionId;
    }

    /**
     * @return the {@link ExecutionListener#EVENTNAME_START event name} in case this execution is passed in for an
     * {@link ExecutionListener}
     */
    public String getEventName() {
        return eventName;
    }

    /**
     * @return Unique id of this path of execution that can be used as a handle to provide external signals back into
     * the engine after wait states.
     */
    public String getId() {
        return id;
    }

    /**
     * @return the Id of the parent activity instance currently executed by this execution.
     */
    public String getParentActivityInstanceId() {
        return parentActivityInstanceId;
    }

    /**
     * @return the id of the parent of this execution. If null, the execution represents a process-instance.
     */
    public String getParentId() {
        return parentId;
    }

    /**
     * @return the business key for the process instance this execution is associated with.
     */
    public String getProcessBusinessKey() {
        return processBusinessKey;
    }

    /**
     * @return the process definition key for the process instance this execution is associated with.
     */
    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    /**
     * @return Reference to the overall process instance
     */
    public String getProcessInstanceId() {
        return processInstanceId;
    }

    /**
     * @return the id of the tenant this execution belongs to. Can be <code>null</code> if the execution belongs to
     * no single tenant.
     */
    public String getTenantId() {
        return tenantId;
    }
}
