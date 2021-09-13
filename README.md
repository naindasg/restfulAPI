**Tasks**
The School of Informatics has hired you to develop a system to keep modules' learning materials well organised and up to date. They are interested in keeping track of modules, who teaches them, and the lectures taught in each module. The data they are interested in keeping track of is as follows:

Modules: Module id (a unique identifier), module code (e.g., "CO2103", "CO3095"), title (e.g., "Software Architecture", "Calculus I"), the semester the module is taught at (an integer between 1 and 10), whether it is a core module or not (i.e., true or false).
Lectures: Lecture id (a unique identifier), week (an integer which defines the order in which a lecture is taught in a module), title (e.g., "Unit Testing"), and a url representing the online location of the slides for this lecture (no files, just the url).
Convenors: Convenor id (a unique identifier), name (e.g., "Jose Rojas"), position (either "GTA", "Lecturer" or "Professor") and office (e.g., "F27").
The API should provide the options of creating (POST), retrieving (GET), editing (PUT/PATCH) and deleting (DELETE) existing modules, lectures and convenors. In other words, these are the resources that the API should provide endpoints for (including collections).

Besides the internal data needed for each resource as described above, there are evident relationships between these resources:

A module is taught by one convenor
A module is composed of an ordered sequence of lectures
A convenor can teach several modules
A lecture belongs to one specific module only

**SOLUTION:**

**Designed and documented a restAPI using swaggerHub for a module-management system:**
https://app.swaggerhub.com/apis/le789/SchoolOfInformatics/1.0.0

**Implementation of the RESTAPI into spring tool suite:**

**Written an executable test and one curl example for each endpoint**

