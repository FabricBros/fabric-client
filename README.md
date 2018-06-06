# fabric-client

# implementation standards
- Logger, use Apache log4j.

        private static Logger logger = Logger.getLogger(Class.class);

- Exception Handling
    catch, log then throw again. Let all exceptions propogate upstream to caller.
    Do not throw Super class Exception, throw something else with more granular information.
    
        try{
            object.call(fcn);
        }catch(IOException e){
            logger.error(e);
            throw e;
        }

- Separate responsibilities into smaller methods

- Format for Config properties should follow this format:

MHC_FABRIC_\<PACKAGE\>\_\<CLASS>_\<PROPERTY>

For example:

    MHC_FABRIC_NOSQL_CLOUDANTDB_ID
    MHC_FABRIC_NOSQL_CLOUDANTDB_SECRET
    
- Install SonarLint pluggin and analyze your code and fix all major and critical issues before pushing.

