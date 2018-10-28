/*
 *     Copyright (c) 2018, The Eduard Burenkov (http://edevapps.com)
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package com.edevapps.jira.plugins.customfields.lastcomment.impl;

import static com.edevapps.jira.plugins.customfields.lastcomment.impl.LastCommentPluginComponentImpl.COMPONENT_NAME;

import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.ApplicationProperties;
import com.edevapps.jira.plugins.customfields.lastcomment.api.LastCommentPluginComponent;
import javax.inject.Inject;
import javax.inject.Named;

@ExportAsService ({LastCommentPluginComponent.class})
@Named (COMPONENT_NAME)
public class LastCommentPluginComponentImpl implements LastCommentPluginComponent
{
    public static final String COMPONENT_NAME = "lastCommentPluginComponent";

    @ComponentImport
    private final ApplicationProperties applicationProperties;

    @Inject
    public LastCommentPluginComponentImpl(final ApplicationProperties applicationProperties)
    {
        this.applicationProperties = applicationProperties;
    }

    public String getName()
    {
        if(null != applicationProperties)
        {
            return COMPONENT_NAME + ":" + applicationProperties.getDisplayName();
        }
        
        return COMPONENT_NAME;
    }
}