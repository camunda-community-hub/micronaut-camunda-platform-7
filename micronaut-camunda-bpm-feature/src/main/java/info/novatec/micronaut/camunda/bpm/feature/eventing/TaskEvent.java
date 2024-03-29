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

import org.camunda.bpm.engine.delegate.DelegateTask;

import java.util.Date;

/**
 * @author Silvan Brenner
 */
// Implementation based on https://github.com/camunda/camunda-bpm-platform/blob/master/spring-boot-starter/starter/src/main/java/org/camunda/bpm/spring/boot/starter/event/TaskEvent.java
public class TaskEvent {

    protected final String assignee;
    protected final String caseDefinitionId;
    protected final String caseExecutionId;
    protected final String caseInstanceId;
    protected final Date createTime;
    protected final String deleteReason;
    protected final String description;
    protected final Date dueDate;
    protected final String eventName;
    protected final String executionId;
    protected final Date followUpDate;
    protected final String id;
    protected final String name;
    protected final String owner;
    protected final int priority;
    protected final String processDefinitionId;
    protected final String processInstanceId;
    protected final String taskDefinitionKey;
    protected final String tenantId;

    public TaskEvent(DelegateTask delegateTask) {
        this.assignee = delegateTask.getAssignee();
        this.caseDefinitionId = delegateTask.getCaseDefinitionId();
        this.caseExecutionId = delegateTask.getCaseExecutionId();
        this.caseInstanceId = delegateTask.getCaseInstanceId();
        this.createTime = delegateTask.getCreateTime();
        this.deleteReason = delegateTask.getDeleteReason();
        this.description = delegateTask.getDescription();
        this.dueDate = delegateTask.getDueDate();
        this.eventName = delegateTask.getEventName();
        this.executionId = delegateTask.getExecutionId();
        this.followUpDate = delegateTask.getFollowUpDate();
        this.id = delegateTask.getId();
        this.name = delegateTask.getName();
        this.owner = delegateTask.getOwner();
        this.priority = delegateTask.getPriority();
        this.processDefinitionId = delegateTask.getProcessDefinitionId();
        this.processInstanceId = delegateTask.getProcessInstanceId();
        this.taskDefinitionKey = delegateTask.getTaskDefinitionKey();
        this.tenantId = delegateTask.getTenantId();
    }

    /**
     * @return the userId of the person to which this task is delegated.
     */
    public String getAssignee() {
        return assignee;
    }

    /**
     * @return Reference to the case definition or null if it is not related to a case.
     */
    public String getCaseDefinitionId() {
        return caseDefinitionId;
    }

    /**
     * @return Reference to the case execution or null if it is not related to a case instance.
     */
    public String getCaseExecutionId() {
        return caseExecutionId;
    }

    /**
     * @return Reference to the case instance or null if it is not related to a case instance.
     */
    public String getCaseInstanceId() {
        return caseInstanceId;
    }

    /**
     * @return the date/time when this task was created
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @return get delete reason of the task.
     */
    public String getDeleteReason() {
        return deleteReason;
    }

    /**
     * @return Free text description of the task.
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return Due date of the task.
     */
    public Date getDueDate() {
        return dueDate;
    }

    /**
     * @return the event name which triggered the task listener to fire for this task.
     */
    public String getEventName() {
        return eventName;
    }

    /**
     * @return Reference to the path of execution or null if it is not related to a process instance.
     */
    public String getExecutionId() {
        return executionId;
    }

    /**
     * @return Follow-up date of the task.
     */
    public Date getFollowUpDate() {
        return followUpDate;
    }

    /**
     * @return DB id of the task.
     */
    public String getId() {
        return id;
    }

    /**
     * @return Name or title of the task.
     */
    public String getName() {
        return name;
    }

    /**
     * @return the userId of the person responsible for this task.
     */
    public String getOwner() {
        return owner;
    }

    /**
     * @return indication of how important/urgent this task is with a number between 0 and 100 where higher values
     * mean a higher priority and lower values mean lower priority: [0..19] lowest, [20..39] low, [40..59] normal,[60..79] high,[80..100] highest
     */
    public int getPriority() {
        return priority;
    }

    /**
     * @return Reference to the process definition or null if it is not related to a process.
     */
    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    /**
     * @return Reference to the process instance or null if it is not related to a process instance.
     */
    public String getProcessInstanceId() {
        return processInstanceId;
    }

    /**
     * @return the id of the activity in the process defining this task or null if this is not related to a process
     */
    public String getTaskDefinitionKey() {
        return taskDefinitionKey;
    }

    /**
     * @return the id of the tenant this task belongs to. Can be <code>null</code> if the task belongs to no single
     * tenant.
     */
    public String getTenantId() {
        return tenantId;
    }
}
