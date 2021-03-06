/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.sshd.common.io.mina;

import java.util.HashMap;
import java.util.Map;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.service.IoService;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.sshd.common.FactoryManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 */
public abstract class MinaService implements org.apache.sshd.common.io.IoService, IoHandler {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    protected final FactoryManager manager;
    protected final org.apache.sshd.common.io.IoHandler handler;

    public MinaService(FactoryManager manager, org.apache.sshd.common.io.IoHandler handler) {
        this.manager = manager;
        this.handler = handler;
    }

    public int getNioWorkers() {
        String nioWorkers = manager.getProperties().get(FactoryManager.NIO_WORKERS);
        if (nioWorkers != null && nioWorkers.length() > 0) {
            int nb = Integer.parseInt(nioWorkers);
            if (nb > 0) {
                return nb;
            }
        }
        return FactoryManager.DEFAULT_NIO_WORKERS;
    }

    protected abstract IoService getIoService();

    public void dispose() {
        getIoService().dispose();
    }

    public Map<Long, org.apache.sshd.common.io.IoSession> getManagedSessions() {
        Map<Long, IoSession> mina = new HashMap<Long, IoSession>(getIoService().getManagedSessions());
        Map<Long, org.apache.sshd.common.io.IoSession> sessions = new HashMap<Long, org.apache.sshd.common.io.IoSession>();
        for (Long id : mina.keySet()) {
            sessions.put(id, getSession(mina.get(id)));
        }
        return sessions;
    }

    public void sessionCreated(IoSession session) throws Exception {
        org.apache.sshd.common.io.IoSession ioSession = new MinaSession(this, session);
        session.setAttribute(org.apache.sshd.common.io.IoSession.class, ioSession);
        handler.sessionCreated(ioSession);
    }

    public void sessionOpened(IoSession session) throws Exception {
    }

    public void sessionClosed(IoSession session) throws Exception {
        handler.sessionClosed(getSession(session));
    }

    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
    }

    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        handler.exceptionCaught(getSession(session), cause);
    }

    public void messageReceived(IoSession session, Object message) throws Exception {
        handler.messageReceived(getSession(session), MinaSupport.asReadable((IoBuffer) message));
    }

    public void messageSent(IoSession session, Object message) throws Exception {
    }

    protected org.apache.sshd.common.io.IoSession getSession(IoSession session) {
        return (org.apache.sshd.common.io.IoSession)
                session.getAttribute(org.apache.sshd.common.io.IoSession.class);
    }
}
