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

package com.edevapps.jira.plugins.customfields.lastcomment;

import static com.edevapps.jira.util.PropertiesUtil.getDateCompleteFormat;
import static com.edevapps.util.AssertUtil.assertNotNull;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.comments.Comment;
import com.atlassian.jira.issue.comments.CommentManager;
import com.atlassian.jira.issue.customfields.impl.CalculatedCFType;
import com.atlassian.jira.issue.customfields.impl.FieldValidationException;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.issue.fields.layout.field.FieldLayoutItem;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import java.text.SimpleDateFormat;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Scanned
public class LastCommentCustomField extends CalculatedCFType {

  private static final String EMPTY_STRING = "";
  private static final String DATE_PAR = "date";
  private static final String USER_PAR = "user";

  private final CommentManager commentManager;

  public LastCommentCustomField(@ComponentImport JiraAuthenticationContext authenticationContext,
      @ComponentImport CommentManager commentManager) {
    super();
    this.commentManager = commentManager;
  }

  private String getCommentDate(Comment comment) {
    if (comment.getUpdated().after(comment.getCreated())) {
      return new SimpleDateFormat(getDateCompleteFormat()).format(comment.getUpdated());
    } else {
      return new SimpleDateFormat(getDateCompleteFormat()).format(comment.getCreated());
    }
  }

  @Override
  @Nonnull
  public Map<String, Object> getVelocityParameters(final Issue issue, final CustomField field,
      final FieldLayoutItem fieldLayoutItem) {
    final Map<String, Object> map = assertNotNull(
        super.getVelocityParameters(issue, field, fieldLayoutItem), "getVelocityParameters(...)");
    Comment lastComment = this.commentManager.getLastComment(issue);
    if (issue == null || lastComment == null) {
      return map;
    }
    map.put(DATE_PAR, getCommentDate(lastComment));
    map.put(USER_PAR, lastComment.getAuthorFullName());

    return map;
  }

  @Override
  public String getStringFromSingularObject(Object o) {
    return o.toString();
  }

  @Override
  public Object getSingularObjectFromString(String s) throws FieldValidationException {
    return s;
  }

  @Nullable
  @Override
  public Object getValueFromIssue(CustomField customField, Issue issue) {
    Comment comment = this.commentManager.getLastComment(issue);
    return comment == null ? EMPTY_STRING : comment.getBody();
  }
}