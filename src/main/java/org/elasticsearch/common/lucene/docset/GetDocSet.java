/*
 * Licensed to ElasticSearch and Shay Banon under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. ElasticSearch licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.common.lucene.docset;

import org.apache.lucene.search.DocIdSetIterator;

import java.io.IOException;

/**
 *
 */
public abstract class GetDocSet extends DocSet {

    private final int maxDoc;

    protected GetDocSet(int maxDoc) {
        this.maxDoc = maxDoc;
    }

    @Override
    public long sizeInBytes() {
        return 0;
    }

    @Override
    public int length() {
        return maxDoc;
    }

    @Override
    public DocIdSetIterator iterator() throws IOException {
        return new DocIdSetIterator() {
            private int doc = -1;

            @Override
            public int docID() {
                return doc;
            }

            @Override
            public int nextDoc() throws IOException {
                do {
                    doc++;
                    if (doc >= maxDoc) {
                        return doc = NO_MORE_DOCS;
                    }
                } while (!get(doc));
                return doc;
            }

            @Override
            public int advance(int target) throws IOException {
                if (target >= maxDoc) {
                    return doc = NO_MORE_DOCS;
                }
                doc = target;
                while (!get(doc)) {
                    doc++;
                    if (doc >= maxDoc) {
                        return doc = NO_MORE_DOCS;
                    }
                }
                return doc;
            }
        };
    }
}
