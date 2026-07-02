# XXF Android Library Skills

This folder contains module-specific skills for published XXF Android libraries.

These skills are written from the external Maven consumer perspective: public API, dependency relationship, and minimum usage examples. They should not include private repository build, verification, release, or maintenance commands.

## Companion Shared Skills

Generic Android workflow rules live in a separate repository:

```text
https://github.com/NBXXF/android-skills
```

Install both skill libraries when a coding agent should understand both generic Android engineering rules and XXF library-specific usage.

## Install

Install shared Android engineering skills first. Run project-scoped installs from the Android project root where the agent should discover the skills:

```bash
git clone https://github.com/NBXXF/android-skills.git /path/to/android-skills
cd /path/to/target-android-project
bash /path/to/android-skills/install.sh codex project
```

Install XXF Android library skills from this repository:

```bash
git clone https://github.com/NBXXF/xxf_android.git /path/to/xxf_android
cd /path/to/target-android-project
bash /path/to/xxf_android/skills/install.sh codex project
```

Supported agents:

```bash
bash /path/to/xxf_android/skills/install.sh codex project
bash /path/to/xxf_android/skills/install.sh codex user
bash /path/to/xxf_android/skills/install.sh claude user
bash /path/to/xxf_android/skills/install.sh cursor project
```

Install targets:

- Codex project install creates symlinks in `.agents/skills` and adds a small managed block to `AGENTS.md`.
- Codex user install creates symlinks in `$HOME/.agents/skills`.
- Claude install creates symlinks in `.claude/skills`.
- Cursor project install copies rules to `.cursor/rules`.

## Boundaries

- `android-skills`: shared Android workflow, architecture, test, review, risk, performance, and clarification rules.
- `xxf_android/skills`: concrete published library/module usage skills.
- Do not add `xxf-aaa-*` shared workflow skills back into this folder.
