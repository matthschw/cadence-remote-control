/**
 * This package contains classes to create sessions with Cadence tools.
 * 
 * <p>
 * 
 * There are some environment variables to control the behaviour:
 * 
 * <p>
 * <code>ED_CDS_RC_NO_WATCHDOG</code>
 * <p>
 * 
 * By default, a watchdog is created for every
 * {@link edlab.eda.cadence.rc.session.SkillInteractiveSession} session. The
 * watchdog will terminate the session when no communication within the
 * specified timeout was conducted.
 * 
 * This behavior can be deactivated either by specifying a negative timeout via
 * {@link edlab.eda.cadence.rc.session.SkillInteractiveSession#setWatchdogTimeout}
 * or by setting the environment variable <code>ED_CDS_RC_NO_WATCHDOG</code> to
 * any value, e.g. in bash syntax
 * 
 * <pre>
 * export ED_CDS_RC_NO_WATCHDOG=1
 * </pre>
 * 
 * <p>
 * <code>ED_CDS_RC_PROMPT</code>
 * <p>
 * This environment variable is needed to customize a
 * {@link edlab.eda.cadence.rc.session.SkillInteractiveSession}. As a default
 * value, the corresponding Virtuoso session notifies when a new command can be
 * fowared to the REPL by printing the greater-tran character {@code >} to the
 * prompt. This tool uses this character to identify that the execution of a
 * previous command is finished. Sometimes, this charcter is redifined by the
 * CAD department. In this case, this tool must informed about this new
 * character by setting <code>ED_CDS_RC_PROMPT</code> to the corresponding
 * value.
 * <p>
 * You can identify this charcter by typing the command
 * <code>getPrompts()</code> to the corresponding Cadence tool. As a
 * consequence, a list of two strings is shown, e.g, .{<code>(">" "$")}. The
 * second list element corresponds to the character of interest for this
 * purpose. In this case, set this environment variable beforehand, e.g.
 * 
 * <pre>
 * export ED_CDS_RC_PROMPT=$
 * </pre>
 * 
 * <p>
 * <code>ED_CDS_LOG</code>
 * <p>
 * 
 * When you set the environment variable <code>ED_CDS_LOG</code> to any value, a
 * {@link edlab.eda.cadence.rc.session.SkillSession} will create a logfile. This
 * is needed for debugging.
 */
package edlab.eda.cadence.rc.session;