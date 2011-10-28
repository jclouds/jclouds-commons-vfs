/**
 *
 * Copyright (C) 2009 Cloud Conscious, LLC. <info@cloudconscious.com>
 *
 * ====================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ====================================================================
 */
package org.jclouds.vfs.provider.blobstore.test;

import org.apache.commons.vfs2.test.ContentTests;
import org.apache.commons.vfs2.test.LastModifiedTests;
import org.apache.commons.vfs2.test.NamingTests;
import org.apache.commons.vfs2.test.ProviderCacheStrategyTests;
import org.apache.commons.vfs2.test.ProviderDeleteTests;
import org.apache.commons.vfs2.test.ProviderRandomReadTests;
import org.apache.commons.vfs2.test.ProviderRandomReadWriteTests;
import org.apache.commons.vfs2.test.ProviderReadTests;
import org.apache.commons.vfs2.test.ProviderRenameTests;
import org.apache.commons.vfs2.test.ProviderTestConfig;
import org.apache.commons.vfs2.test.ProviderTestSuite;
import org.apache.commons.vfs2.test.ProviderWriteAppendTests;
import org.apache.commons.vfs2.test.ProviderWriteTests;
import org.apache.commons.vfs2.test.UriTests;
import org.apache.commons.vfs2.test.UrlStructureTests;
import org.apache.commons.vfs2.test.UrlTests;

/**
 * @author Adrian Cole
 */
public class BlobStoreProviderTestSuite extends ProviderTestSuite {

   public BlobStoreProviderTestSuite(ProviderTestConfig providerConfig) throws Exception {
      super(providerConfig, "", false, false);
   }

   /**
    * Adds base tests - excludes the nested test cases.
    */
   protected void addBaseTests() throws Exception {
      addTests(ProviderCacheStrategyTests.class);
      addTests(UriTests.class);
      addTests(NamingTests.class);
      addTests(ContentTests.class);
      addTests(ProviderReadTests.class);
      addTests(ProviderRandomReadTests.class);
      addTests(ProviderWriteTests.class);
      addTests(ProviderWriteAppendTests.class);
      addTests(ProviderRandomReadWriteTests.class);
      addTests(ProviderRenameTests.class);
      addTests(ProviderDeleteTests.class);
      addTests(LastModifiedTests.class);
      addTests(UrlTests.class);
      addTests(UrlStructureTests.class);
      // The class loader test requires the classes be uploaded to the webdav repo.
      // addTests(VfsClassLoaderTests.class);
   }
}