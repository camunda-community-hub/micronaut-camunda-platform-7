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

import info.novatec.micronaut.camunda.bpm.feature.Configuration;
import io.micronaut.context.event.ApplicationEventPublisher;
import jakarta.inject.Singleton;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.camunda.bpm.engine.impl.bpmn.parser.BpmnParseListener;
import org.camunda.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.camunda.bpm.engine.impl.pvm.process.ActivityImpl;
import org.camunda.bpm.engine.impl.pvm.process.ScopeImpl;
import org.camunda.bpm.engine.impl.pvm.process.TransitionImpl;
import org.camunda.bpm.engine.impl.task.TaskDefinition;
import org.camunda.bpm.engine.impl.util.xml.Element;
import org.camunda.bpm.engine.impl.variable.VariableDeclaration;

import java.util.Arrays;
import java.util.List;

import static org.camunda.bpm.engine.delegate.ExecutionListener.*;
import static org.camunda.bpm.engine.delegate.TaskListener.*;

/**
 * Parse listener adding provided execution and task listeners.
 *
 * @author Silvan Brenner
 */
// Implementation based on https://github.com/camunda/camunda-bpm-platform/blob/master/spring-boot-starter/starter/src/main/java/org/camunda/bpm/spring/boot/starter/event/PublishDelegateParseListener.java
@Singleton
public class PublishDelegateParseListener implements BpmnParseListener {

    protected static final List<String> TASK_EVENTS = Arrays.asList(
            EVENTNAME_COMPLETE,
            EVENTNAME_ASSIGNMENT,
            EVENTNAME_CREATE,
            EVENTNAME_DELETE,
            EVENTNAME_UPDATE);
    protected static final List<String> EXECUTION_EVENTS = Arrays.asList(
            EVENTNAME_START,
            EVENTNAME_END);

    protected final TaskListener taskListener;
    protected final ExecutionListener executionListener;

    public PublishDelegateParseListener(Configuration configuration, ApplicationEventPublisher publisher) {
        if (configuration.getEventing().isExecution()) {
            executionListener = delegateExecution ->
                    publisher.publishEvent(new ExecutionEvent(delegateExecution));
        } else {
            executionListener = null;
        }
        if (configuration.getEventing().isTask()) {
            taskListener = delegateTask ->
                    publisher.publishEvent(new TaskEvent(delegateTask));
        } else {
            taskListener = null;
        }
    }

    @Override
    public void parseUserTask(Element userTaskElement, ScopeImpl scope, ActivityImpl activity) {
        addTaskListener(taskDefinition(activity));
        addExecutionListener(activity);
    }

    @Override
    public void parseBoundaryErrorEventDefinition(Element errorEventDefinition, boolean interrupting, ActivityImpl activity, ActivityImpl nestedErrorEventActivity) {
        // Do not implement. Start and end event listener are set by parseBoundaryEvent()
    }

    @Override
    public void parseBoundaryEvent(Element boundaryEventElement, ScopeImpl scopeElement, ActivityImpl activity) {
        addExecutionListener(activity);
    }

    @Override
    public void parseBoundaryMessageEventDefinition(Element element, boolean interrupting, ActivityImpl activity) {
        // Do not implement. Start and end event listener are set by parseBoundaryEvent()
    }

    @Override
    public void parseBoundarySignalEventDefinition(Element signalEventDefinition, boolean interrupting, ActivityImpl activity) {
        // Do not implement. Start and end event listener are set by parseBoundaryEvent()
    }

    @Override
    public void parseBoundaryTimerEventDefinition(Element timerEventDefinition, boolean interrupting, ActivityImpl activity) {
        // Do not implement. Start and end event listener are set by parseBoundaryEvent()
    }

    @Override
    public void parseBusinessRuleTask(Element businessRuleTaskElement, ScopeImpl scope, ActivityImpl activity) {
        addExecutionListener(activity);
    }

    @Override
    public void parseCallActivity(Element callActivityElement, ScopeImpl scope, ActivityImpl activity) {
        addExecutionListener(activity);
    }

    @Override
    public void parseCompensateEventDefinition(Element compensateEventDefinition, ActivityImpl activity) {
        // Do not implement. Start and end event listener are set by parseBoundaryEvent()
    }

    @Override
    public void parseEndEvent(Element endEventElement, ScopeImpl scope, ActivityImpl activity) {
        addExecutionListener(activity);
    }

    @Override
    public void parseEventBasedGateway(Element eventBasedGwElement, ScopeImpl scope, ActivityImpl activity) {
        addExecutionListener(activity);
    }

    @Override
    public void parseExclusiveGateway(Element exclusiveGwElement, ScopeImpl scope, ActivityImpl activity) {
        addExecutionListener(activity);
    }

    @Override
    public void parseInclusiveGateway(Element inclusiveGwElement, ScopeImpl scope, ActivityImpl activity) {
        addExecutionListener(activity);
    }

    @Override
    public void parseIntermediateCatchEvent(Element intermediateEventElement, ScopeImpl scope, ActivityImpl activity) {
        addExecutionListener(activity);
    }

    @Override
    public void parseIntermediateMessageCatchEventDefinition(Element messageEventDefinition, ActivityImpl activity) {
        // Do not implement. Start and end event listener are set by parseIntermediateCatchEvent()
    }

    @Override
    public void parseIntermediateSignalCatchEventDefinition(Element signalEventDefinition, ActivityImpl activity) {
        // Do not implement. Start and end event listener are set by parseIntermediateCatchEvent()
    }

    @Override
    public void parseIntermediateThrowEvent(Element intermediateEventElement, ScopeImpl scope, ActivityImpl activity) {
        addExecutionListener(activity);
    }

    @Override
    public void parseIntermediateTimerEventDefinition(Element timerEventDefinition, ActivityImpl activity) {
        // Do not implement. Start and end event listener are set by parseIntermediateCatchEvent()
    }


    @Override
    public void parseManualTask(Element manualTaskElement, ScopeImpl scope, ActivityImpl activity) {
        addExecutionListener(activity);
    }

    @Override
    public void parseMultiInstanceLoopCharacteristics(Element activityElement, Element multiInstanceLoopCharacteristicsElement, ActivityImpl activity) {
        // Do not implement
        // we do not notify on entering a multi-instance activity, this will be done for every single execution inside that loop.
    }

    @Override
    public void parseParallelGateway(Element parallelGwElement, ScopeImpl scope, ActivityImpl activity) {
        addExecutionListener(activity);
    }

    @Override
    public void parseProcess(Element processElement, ProcessDefinitionEntity processDefinition) {
        if (executionListener != null) {
            for (String event : EXECUTION_EVENTS) {
                processDefinition.addListener(event, executionListener);
            }
        }
    }

    @Override
    public void parseReceiveTask(Element receiveTaskElement, ScopeImpl scope, ActivityImpl activity) {
        addExecutionListener(activity);
    }

    @Override
    public void parseScriptTask(Element scriptTaskElement, ScopeImpl scope, ActivityImpl activity) {
        addExecutionListener(activity);
    }

    @Override
    public void parseSendTask(Element sendTaskElement, ScopeImpl scope, ActivityImpl activity) {
        addExecutionListener(activity);
    }

    @Override
    public void parseSequenceFlow(Element sequenceFlowElement, ScopeImpl scopeElement, TransitionImpl transition) {
        addExecutionListener(transition);
    }

    @Override
    public void parseServiceTask(Element serviceTaskElement, ScopeImpl scope, ActivityImpl activity) {
        addExecutionListener(activity);
    }

    @Override
    public void parseStartEvent(Element startEventElement, ScopeImpl scope, ActivityImpl activity) {
        addExecutionListener(activity);
    }

    @Override
    public void parseSubProcess(Element subProcessElement, ScopeImpl scope, ActivityImpl activity) {
        addExecutionListener(activity);
    }

    @Override
    public void parseTask(Element taskElement, ScopeImpl scope, ActivityImpl activity) {
        addExecutionListener(activity);
    }

    @Override
    public void parseTransaction(Element transactionElement, ScopeImpl scope, ActivityImpl activity) {
        addExecutionListener(activity);
    }

    @Override
    public void parseBoundaryEscalationEventDefinition(Element escalationEventDefinition, boolean interrupting, ActivityImpl boundaryEventActivity) {
        // Do not implement. Start and end event listener are set by parseBoundaryEvent()
    }

    @Override
    public void parseBoundaryConditionalEventDefinition(Element element, boolean interrupting, ActivityImpl conditionalActivity) {
        // Do not implement. Start and end event listener are set by parseBoundaryEvent()
    }

    @Override
    public void parseIntermediateConditionalEventDefinition(Element conditionalEventDefinition, ActivityImpl conditionalActivity) {
        // Do not implement. Start and end event listener are set by parseBoundaryEvent()
    }

    @Override
    public void parseConditionalStartEventForEventSubprocess(Element element, ActivityImpl conditionalActivity, boolean interrupting) {
        // Do not implement. Start and end event listener are set by parseBoundaryEvent()
    }

    @Override
    public void parseRootElement(Element rootElement, List<ProcessDefinitionEntity> processDefinitions) {
        // Do not implement
    }

    @Override
    public void parseProperty(Element propertyElement, VariableDeclaration variableDeclaration, ActivityImpl activity) {
        // Do not implement
    }

    protected void addExecutionListener(ActivityImpl activity) {
        if (executionListener != null) {
            for (String event : EXECUTION_EVENTS) {
                activity.addListener(event, executionListener);
            }
        }
    }

    protected void addExecutionListener(TransitionImpl transition) {
        if (executionListener != null) {
            transition.addListener(EVENTNAME_TAKE, executionListener);
        }
    }

    protected void addTaskListener(TaskDefinition taskDefinition) {
        if (taskListener != null) {
            for (String event : TASK_EVENTS) {
                taskDefinition.addTaskListener(event, taskListener);
            }
        }
    }

    /**
     * Retrieves task definition.
     *
     * @param activity the taskActivity
     * @return taskDefinition for activity
     */
    protected TaskDefinition taskDefinition(ActivityImpl activity) {
        return ((UserTaskActivityBehavior) activity.getActivityBehavior()).getTaskDefinition();
    }
}
