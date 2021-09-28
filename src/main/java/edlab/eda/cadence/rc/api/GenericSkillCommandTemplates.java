package edlab.eda.cadence.rc.api;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Container for generic SKILL command templates
 *
 */
public class GenericSkillCommandTemplates {

  public static final String PLUS = "plus";

  public static final String SET = "set";
  public static final String GET = "get";

  public static final String CAR = "car";
  public static final String CADR = "cadr";
  public static final String NTH = "nth";
  public static final String LIST = "list";

  public static final String APPLY = "apply";
  public static final String EVAL = "eval";
  public static final String IS_CALLABLE = "isCallable";

  public static final String ARRAYREF = "arrayref";

  public static final String OUTFILE = "outfile";
  public static final String FPRINTF = "fprintf";
  public static final String CLOSE = "close";
  public static final String STRCAT = "strcat";

  public static final String PROGN = "progn";
  public static final String LOADI = "loadi";
  public static final String ERRSET = "errset";
  public static final String EXIT = "exit";
  public static final String LOAD = "load";
  public static final String LOAD_CONTEXT = "loadContext";

  public static final String SET_PROMPTS = "setPrompts";

  public static final String GET_INSTALL_PATH = "getInstallPath";
  public static final String GET_WORKING_DIR = "getWorkingDir";
  public static final String GET_LOGIN = "getLogin";
  public static final String GET_CURRENT_TIME = "getCurrentTime";

  public static final String ED_CDS_RC_FOMAT_COMMAND = "EDcdsRCfmtCmd";
  public static final String ED_CDS_RC_BUILD_XML = "EDcdsRCbuildXML";
  public static final String ED_CDS_RC_ESCPAE_XML = "EDcdsRCescapeXML";
  public static final String ED_CDS_RC_EXECUTE_COMMAND_FROM_FILE = "EDcdsRCexCmdFile";

  private static GenericSkillCommandTemplates commandTemplates = null;

  private Map<String, SkillCommandTemplate> templates;

  private GenericSkillCommandTemplates() {

    Set<String> keywordParameters;

    templates = new HashMap<String, SkillCommandTemplate>();

    templates.put(GenericSkillCommandTemplates.PLUS,
        new SkillCommandTemplate(GenericSkillCommandTemplates.PLUS, 2, true));

    templates.put(GenericSkillCommandTemplates.SET,
        new SkillCommandTemplate(GenericSkillCommandTemplates.SET, 2));

    templates.put(GenericSkillCommandTemplates.GET,
        new SkillCommandTemplate(GenericSkillCommandTemplates.GET, 2));

    templates.put(GenericSkillCommandTemplates.CAR,
        new SkillCommandTemplate(GenericSkillCommandTemplates.CAR, 1));
    templates.put(GenericSkillCommandTemplates.CADR,
        new SkillCommandTemplate(GenericSkillCommandTemplates.CADR, 1));
    templates.put(GenericSkillCommandTemplates.NTH,
        new SkillCommandTemplate(GenericSkillCommandTemplates.NTH, 2));

    templates.put(GenericSkillCommandTemplates.LIST,
        new SkillCommandTemplate(GenericSkillCommandTemplates.LIST, true));

    templates.put(GenericSkillCommandTemplates.APPLY,
        new SkillCommandTemplate(GenericSkillCommandTemplates.APPLY, 2));
    templates.put(GenericSkillCommandTemplates.EVAL,
        new SkillCommandTemplate(GenericSkillCommandTemplates.EVAL, 1));
    templates.put(GenericSkillCommandTemplates.IS_CALLABLE,
        new SkillCommandTemplate(GenericSkillCommandTemplates.IS_CALLABLE, 1));    

    templates.put(GenericSkillCommandTemplates.ARRAYREF,
        new SkillCommandTemplate(GenericSkillCommandTemplates.ARRAYREF, 2));

    templates.put(GenericSkillCommandTemplates.OUTFILE,
        new SkillCommandTemplate(GenericSkillCommandTemplates.OUTFILE, 1));

    templates.put(GenericSkillCommandTemplates.FPRINTF,
        new SkillCommandTemplate(GenericSkillCommandTemplates.FPRINTF, 1,
            true));

    templates.put(GenericSkillCommandTemplates.STRCAT,
        new SkillCommandTemplate(GenericSkillCommandTemplates.STRCAT, 1, true));

    templates.put(GenericSkillCommandTemplates.CLOSE,
        new SkillCommandTemplate(GenericSkillCommandTemplates.CLOSE, 1));

    templates.put(GenericSkillCommandTemplates.PROGN,
        new SkillCommandTemplate(GenericSkillCommandTemplates.PROGN, true));

    templates.put(GenericSkillCommandTemplates.ERRSET,
        new SkillCommandTemplate(GenericSkillCommandTemplates.ERRSET, 1));

    templates.put(GenericSkillCommandTemplates.EXIT,
        new SkillCommandTemplate(GenericSkillCommandTemplates.EXIT, 0));

    templates.put(GenericSkillCommandTemplates.LOAD,
        new SkillCommandTemplate(GenericSkillCommandTemplates.LOAD, 1));

    templates.put(GenericSkillCommandTemplates.LOADI,
        new SkillCommandTemplate(GenericSkillCommandTemplates.LOADI, 1));

    templates.put(GenericSkillCommandTemplates.LOAD_CONTEXT,
        new SkillCommandTemplate(GenericSkillCommandTemplates.LOAD_CONTEXT, 1));

    templates.put(GenericSkillCommandTemplates.SET_PROMPTS,
        new SkillCommandTemplate(GenericSkillCommandTemplates.SET_PROMPTS, 2));

    templates.put(GenericSkillCommandTemplates.GET_INSTALL_PATH,
        new SkillCommandTemplate(
            GenericSkillCommandTemplates.GET_INSTALL_PATH));
    templates.put(GenericSkillCommandTemplates.GET_WORKING_DIR,
        new SkillCommandTemplate(GenericSkillCommandTemplates.GET_WORKING_DIR));
    templates.put(GenericSkillCommandTemplates.GET_LOGIN,
        new SkillCommandTemplate(GenericSkillCommandTemplates.GET_LOGIN));
    templates.put(GenericSkillCommandTemplates.GET_CURRENT_TIME,
        new SkillCommandTemplate(
            GenericSkillCommandTemplates.GET_CURRENT_TIME));

    keywordParameters = new HashSet<String>();
    keywordParameters.add("returnType");
    keywordParameters.add("session");

    templates.put(GenericSkillCommandTemplates.ED_CDS_RC_FOMAT_COMMAND,
        KeywordParameterSkillCommandTemplate.build(
            GenericSkillCommandTemplates.ED_CDS_RC_FOMAT_COMMAND, 1,
            keywordParameters));

    templates.put(
        GenericSkillCommandTemplates.ED_CDS_RC_EXECUTE_COMMAND_FROM_FILE,
        KeywordParameterSkillCommandTemplate.build(
            GenericSkillCommandTemplates.ED_CDS_RC_EXECUTE_COMMAND_FROM_FILE, 1,
            keywordParameters));

    templates.put(GenericSkillCommandTemplates.ED_CDS_RC_BUILD_XML,
        KeywordParameterSkillCommandTemplate.build(
            GenericSkillCommandTemplates.ED_CDS_RC_BUILD_XML, 1,
            keywordParameters));

    templates.put(GenericSkillCommandTemplates.ED_CDS_RC_ESCPAE_XML,
        new SkillCommandTemplate(
            GenericSkillCommandTemplates.ED_CDS_RC_ESCPAE_XML, 1));

  }

  /**
   * Get a {@link SkillCommandTemplate} by name
   * 
   * @param name Name of the command
   * @return template when existing,<code>null</code> otherwise
   */
  public static SkillCommandTemplate getTemplate(String name) {

    if (commandTemplates == null) {
      commandTemplates = new GenericSkillCommandTemplates();
    }

    return commandTemplates.templates.get(name);
  }
}