/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shardingsphere.proxy.backend.handler.distsql.ral.updatable;

import org.apache.shardingsphere.infra.exception.core.external.sql.type.generic.UnsupportedSQLOperationException;
import org.apache.shardingsphere.infra.metadata.database.ShardingSphereDatabase;
import org.apache.shardingsphere.mode.manager.ContextManager;
import org.apache.shardingsphere.proxy.backend.context.ProxyContext;
import org.apache.shardingsphere.proxy.backend.handler.distsql.ral.updatable.readwritesplitting.AlterReadwriteSplittingStorageUnitStatusExecutor;
import org.apache.shardingsphere.readwritesplitting.distsql.statement.status.AlterReadwriteSplittingStorageUnitStatusStatement;
import org.apache.shardingsphere.sql.parser.sql.common.segment.generic.DatabaseSegment;
import org.apache.shardingsphere.sql.parser.sql.common.value.identifier.IdentifierValue;
import org.apache.shardingsphere.test.mock.AutoMockExtension;
import org.apache.shardingsphere.test.mock.StaticMockSettings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(AutoMockExtension.class)
@StaticMockSettings(ProxyContext.class)
class AlterReadwriteSplittingStorageUnitStatusStatementUpdaterTest {
    
    @Test
    void assertWithStandaloneMode() {
        AlterReadwriteSplittingStorageUnitStatusExecutor executor = new AlterReadwriteSplittingStorageUnitStatusExecutor();
        assertThrows(UnsupportedSQLOperationException.class,
                () -> executor.executeUpdate(new AlterReadwriteSplittingStorageUnitStatusStatement(
                        new DatabaseSegment(1, 1, new IdentifierValue("foo_db")), "group", "read_ds", "ENABLE"), mock(ContextManager.class, RETURNS_DEEP_STUBS)));
    }
    
    @Test
    void assertWithNoReadwriteSplittingRule() {
        ContextManager contextManager = mockContextManager();
        ShardingSphereDatabase database = mock(ShardingSphereDatabase.class, RETURNS_DEEP_STUBS);
        when(ProxyContext.getInstance().getDatabase("foo_db")).thenReturn(database);
        AlterReadwriteSplittingStorageUnitStatusExecutor executor = new AlterReadwriteSplittingStorageUnitStatusExecutor();
        executor.setDatabase(database);
        assertThrows(UnsupportedSQLOperationException.class,
                () -> executor.executeUpdate(new AlterReadwriteSplittingStorageUnitStatusStatement(
                        new DatabaseSegment(1, 1, new IdentifierValue("foo_db")), "group", "read_ds", "ENABLE"), contextManager));
    }
    
    private ContextManager mockContextManager() {
        ContextManager result = mock(ContextManager.class, RETURNS_DEEP_STUBS);
        when(result.getInstanceContext().isCluster()).thenReturn(true);
        return result;
    }
}
