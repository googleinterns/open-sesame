// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.


// DIV_IDs

var iShouldGetFlagged = "potato";
nfjf

/** 
 * The id of the div where the nav bar will be added.
 * @const {String}
 */
const NAV_ID = "my-nav";

/** 
 * Nav-bar anchors in HTML
 * @const {String}
 */
const navBarHTML =
  `
  <a target="_blank" href="mailto: coa22@cornell.edu" class="nav_bar">
    /email_me
  </a>	
  <a target="_blank" href="https://github.com/Obinnabii" class="nav_bar">
    /see_github
  </a>	
  <a target="_blank" href="https://drive.google.com/file/d/1tc2YaWmPzVhdCSb3Gv6VVz-599Qa5CrT/view?usp=sharing" class="nav_bar ">
    /see_resume
  </a>	
  <a target="_self" href="index.html" class="nav_bar">
    /welcome_page
  </a>	
  <a target="_self" href="comments.html" class="nav_bar">
    /comments
  </a>	
  `;

// Add nav bar to each page. This lets us reuse the navBarHTML across all 4 pages
document.getElementById(NAV_ID).innerHTML = navBarHTML;