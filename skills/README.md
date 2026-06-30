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

Install shared Android engineering skills first:

```bash
git clone https://github.com/NBXXF/android-skills.git
cd android-skills
bash install.sh codex project
```

Install XXF Android library skills from this repository:

```bash
git clone https://github.com/NBXXF/xxf_android.git
cd xxf_android/skills
bash install.sh codex project
```

Supported agents:

```bash
bash install.sh codex project
bash install.sh claude user
bash install.sh cursor project
```

## Boundaries

- `android-skills`: shared Android workflow, architecture, test, review, risk, performance, and clarification rules.
- `xxf_android/skills`: concrete published library/module usage skills.
- Do not add `xxf-aaa-*` shared workflow skills back into this folder.
