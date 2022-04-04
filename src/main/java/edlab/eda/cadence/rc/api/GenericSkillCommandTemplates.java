package edlab.eda.cadence.rc.api;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Container for generic Skill command templates
 */
public final class GenericSkillCommandTemplates {

  public static final String PLUS = "plus";
  public static final String SQRT = "sqrt";
  public static final String TIMES = "times";

  public static final String SET = "set";
  public static final String GET = "get";
  public static final String GETQ = "getq";

  public static final String CAR = "car";
  public static final String CADR = "cadr";
  public static final String NTH = "nth";
  public static final String LIST = "list";

  public static final String APPLY = "apply";
  public static final String FUNCALL = "funcall";
  public static final String MAPCAR = "mapcar";
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

  private final Map<String, SkillCommandTemplate> templates;

  private GenericSkillCommandTemplates() {

    Set<String> keywordParameters;

    this.templates = new HashMap<>();

    this.templates.put(GenericSkillCommandTemplates.PLUS,
        new SkillCommandTemplate(GenericSkillCommandTemplates.PLUS, 2, true));

    this.templates.put(GenericSkillCommandTemplates.SQRT,
        new SkillCommandTemplate(GenericSkillCommandTemplates.SQRT, 1));

    this.templates.put(GenericSkillCommandTemplates.TIMES,
        new SkillCommandTemplate(GenericSkillCommandTemplates.TIMES, 2, true));

    this.templates.put(GenericSkillCommandTemplates.SET,
        new SkillCommandTemplate(GenericSkillCommandTemplates.SET, 2));

    this.templates.put(GenericSkillCommandTemplates.GET,
        new SkillCommandTemplate(GenericSkillCommandTemplates.GET, 2));

    this.templates.put(GenericSkillCommandTemplates.GETQ,
        new SkillCommandTemplate(GenericSkillCommandTemplates.GETQ, 2));

    this.templates.put(GenericSkillCommandTemplates.CAR,
        new SkillCommandTemplate(GenericSkillCommandTemplates.CAR, 1));

    this.templates.put(GenericSkillCommandTemplates.CADR,
        new SkillCommandTemplate(GenericSkillCommandTemplates.CADR, 1));

    this.templates.put(GenericSkillCommandTemplates.NTH,
        new SkillCommandTemplate(GenericSkillCommandTemplates.NTH, 2));

    this.templates.put(GenericSkillCommandTemplates.LIST,
        new SkillCommandTemplate(GenericSkillCommandTemplates.LIST, true));

    this.templates.put(GenericSkillCommandTemplates.APPLY,
        new SkillCommandTemplate(GenericSkillCommandTemplates.APPLY, 2));

    this.templates.put(GenericSkillCommandTemplates.MAPCAR,
        new SkillCommandTemplate(GenericSkillCommandTemplates.MAPCAR, 2, true));

    this.templates.put(GenericSkillCommandTemplates.FUNCALL,
        new SkillCommandTemplate(GenericSkillCommandTemplates.FUNCALL, true));

    this.templates.put(GenericSkillCommandTemplates.EVAL,
        new SkillCommandTemplate(GenericSkillCommandTemplates.EVAL, 1));

    this.templates.put(GenericSkillCommandTemplates.IS_CALLABLE,
        new SkillCommandTemplate(GenericSkillCommandTemplates.IS_CALLABLE, 1));

    this.templates.put(GenericSkillCommandTemplates.ARRAYREF,
        new SkillCommandTemplate(GenericSkillCommandTemplates.ARRAYREF, 2));

    this.templates.put(GenericSkillCommandTemplates.OUTFILE,
        new SkillCommandTemplate(GenericSkillCommandTemplates.OUTFILE, 1));

    this.templates.put(GenericSkillCommandTemplates.FPRINTF,
        new SkillCommandTemplate(GenericSkillCommandTemplates.FPRINTF, 1,
            true));

    this.templates.put(GenericSkillCommandTemplates.STRCAT,
        new SkillCommandTemplate(GenericSkillCommandTemplates.STRCAT, 1, true));

    this.templates.put(GenericSkillCommandTemplates.CLOSE,
        new SkillCommandTemplate(GenericSkillCommandTemplates.CLOSE, 1));

    this.templates.put(GenericSkillCommandTemplates.PROGN,
        new SkillCommandTemplate(GenericSkillCommandTemplates.PROGN, true));

    this.templates.put(GenericSkillCommandTemplates.ERRSET,
        new SkillCommandTemplate(GenericSkillCommandTemplates.ERRSET, 1));

    this.templates.put(GenericSkillCommandTemplates.EXIT,
        new SkillCommandTemplate(GenericSkillCommandTemplates.EXIT, 0));

    this.templates.put(GenericSkillCommandTemplates.LOAD,
        new SkillCommandTemplate(GenericSkillCommandTemplates.LOAD, 1));

    this.templates.put(GenericSkillCommandTemplates.LOADI,
        new SkillCommandTemplate(GenericSkillCommandTemplates.LOADI, 1));

    this.templates.put(GenericSkillCommandTemplates.LOAD_CONTEXT,
        OptionalParameterSkillCommandTemplate
            .build(GenericSkillCommandTemplates.LOAD_CONTEXT, 1, 1));

    this.templates.put(GenericSkillCommandTemplates.SET_PROMPTS,
        new SkillCommandTemplate(GenericSkillCommandTemplates.SET_PROMPTS, 2));

    this.templates.put(GenericSkillCommandTemplates.GET_INSTALL_PATH,
        new SkillCommandTemplate(
            GenericSkillCommandTemplates.GET_INSTALL_PATH));
    this.templates.put(GenericSkillCommandTemplates.GET_WORKING_DIR,
        new SkillCommandTemplate(GenericSkillCommandTemplates.GET_WORKING_DIR));
    this.templates.put(GenericSkillCommandTemplates.GET_LOGIN,
        new SkillCommandTemplate(GenericSkillCommandTemplates.GET_LOGIN));
    this.templates.put(GenericSkillCommandTemplates.GET_CURRENT_TIME,
        new SkillCommandTemplate(
            GenericSkillCommandTemplates.GET_CURRENT_TIME));

    keywordParameters = new HashSet<>();
    keywordParameters.add("returnType");
    keywordParameters.add("session");

    this.templates.put(GenericSkillCommandTemplates.ED_CDS_RC_FOMAT_COMMAND,
        KeywordParameterSkillCommandTemplate.build(
            GenericSkillCommandTemplates.ED_CDS_RC_FOMAT_COMMAND, 1,
            keywordParameters));

    this.templates.put(
        GenericSkillCommandTemplates.ED_CDS_RC_EXECUTE_COMMAND_FROM_FILE,
        KeywordParameterSkillCommandTemplate.build(
            GenericSkillCommandTemplates.ED_CDS_RC_EXECUTE_COMMAND_FROM_FILE, 1,
            keywordParameters));

    this.templates.put(GenericSkillCommandTemplates.ED_CDS_RC_BUILD_XML,
        KeywordParameterSkillCommandTemplate.build(
            GenericSkillCommandTemplates.ED_CDS_RC_BUILD_XML, 1,
            keywordParameters));

    this.templates.put(GenericSkillCommandTemplates.ED_CDS_RC_ESCPAE_XML,
        new SkillCommandTemplate(
            GenericSkillCommandTemplates.ED_CDS_RC_ESCPAE_XML, 1));

  }

  /**
   * Get a {@link SkillCommandTemplate} by name
   *
   * @param name Name of the command
   * @return template when existing, <code>null</code> otherwise
   */
  public static SkillCommandTemplate getTemplate(final String name) {

    if (GenericSkillCommandTemplates.commandTemplates == null) {
      GenericSkillCommandTemplates.commandTemplates = new GenericSkillCommandTemplates();
    }

    return GenericSkillCommandTemplates.commandTemplates.templates.get(name);
  }
}