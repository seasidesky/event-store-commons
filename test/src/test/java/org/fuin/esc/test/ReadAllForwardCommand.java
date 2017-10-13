/**
 * Copyright (C) 2015 Michael Schnell. All rights reserved. 
 * http://www.fuin.org/
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. If not, see http://www.gnu.org/licenses/.
 */
package org.fuin.esc.test;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.fuin.esc.api.CommonEvent;
import org.fuin.esc.api.EventStore;
import org.fuin.esc.api.ReadableEventStore;
import org.fuin.esc.api.SimpleStreamId;
import org.fuin.esc.api.StreamId;

/**
 * Reads all events forward from a stream.
 */
public final class ReadAllForwardCommand implements TestCommand {

    private String streamName;

    private int start;

    private int chunkSize;

    private List<ReadAllForwardChunk> expectedChunks;

    // Initialization

    private StreamId streamId;

    private EventStore es;

    // Execution

    private Exception actualException;

    private List<ReadAllForwardChunk> actualChunks;

    /**
     * Constructor for manual creation.
     * 
     * @param streamName
     *            Uniquely identifies the stream to create.
     * @param start
     *            Number of event to start reading.
     * @param chunkSize
     *            Number of event to read in a chunk.
     * @param chunks
     *            Expected chunks.
     */
    public ReadAllForwardCommand(@NotNull final String streamName,
            final int start, final int chunkSize,
            @NotNull final List<ReadAllForwardChunk> chunks) {
        super();
        this.streamName = streamName;
        this.start = start;
        this.chunkSize = chunkSize;
        this.expectedChunks = new ArrayList<>();
        for (final ReadAllForwardChunk chunk : chunks) {
            chunk.getEvents();
            this.expectedChunks.add(chunk);
        }
        this.actualChunks = new ArrayList<>();
    }

    @Override
    public void init(final String currentEventStoreImplType,
            final EventStore eventstore) {
        this.es = eventstore;
        this.streamName = currentEventStoreImplType + "_" + streamName;
        this.streamId = new SimpleStreamId(streamName);
    }

    @Override
    public final void execute() {
        try {
            es.readAllEventsForward(streamId, start, chunkSize,
                    new ReadableEventStore.ChunkEventHandler() {
                        @Override
                        public void handle(final List<CommonEvent> events) {
                            actualChunks.add(new ReadAllForwardChunk(events));
                        }
                    });
        } catch (final Exception ex) {
            this.actualException = ex;
        }
    }

    @Override
    public final boolean isSuccessful() {
        if (actualException != null) {
            return false;
        }
        return expectedChunks.equals(actualChunks);
    }

    @Override
    public final String getFailureDescription() {
        if (actualException != null) {
            return EscTestUtils.createExceptionFailureMessage(streamId,
                    actualException);
        }
        return "[" + streamId + "] expected " + expectedChunks + ", but was: "
                + actualChunks;
    }

    @Override
    public final void verify() {
        if (!isSuccessful()) {
            throw new RuntimeException(getFailureDescription());
        }
    }

    @Override
    public final String toString() {
        return "ReadAllForwardCommand [streamName=" + streamName + ", start="
                + start + ", chunkSize=" + chunkSize + ", expectedChunks="
                + expectedChunks + ", actualChunks=" + actualChunks + "]";
    }

}
