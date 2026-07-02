## XXF Android Coding Workflow

For normal coding tasks in this project, do not wait for the user to explicitly ask for tests, review, or release-risk assessment.

Before implementing a generic bugfix, feature change, refactor, or regression fix, first read the shared Android engineering workflow skill from the installed `android-skills` repository:

- `.agents/skills/xxf-aaa-delivery-loop/SKILL.md` after running `android-skills/install.sh codex project`
- `android-skills/skills/xxf-aaa-delivery-loop/SKILL.md` from a local checkout when the skills have not been installed into this repository

Then load any additional relevant skills:

- Matching module skills under `.agents/skills/xxf-*/SKILL.md` after running `skills/install.sh codex project`, or under `./skills/xxf-*/SKILL.md` when working directly in this repository
- Shared Android engineering constraints under the same `android-skills/skills` directory when relevant:
  - `xxf-aaa-coding-style`
  - `xxf-aaa-coding-arch`
  - `xxf-aaa-class-declaration-guidelines`
  - `xxf-aaa-comment-guidelines`
  - `xxf-aaa-model-naming-guidelines`
  - `xxf-aaa-test-strategy`
  - `xxf-aaa-unit-test-writer`
  - `xxf-aaa-auto-test-orchestrator`
  - `xxf-aaa-code-reviewer`
  - `xxf-aaa-risk-gate`
  - `xxf-aaa-ui-design-alignment`
  - `xxf-aaa-android-performance-gate`
  - `xxf-aaa-module-scaffold`
  - `xxf-aaa-clarify-question`

Default expectation for ordinary coding work in this repo:

1. Make the smallest correct change.
2. Add or repair the minimum effective tests when the change needs regression protection.
3. Run the narrowest relevant Gradle verification.
4. Review the change when risk is non-trivial.
5. Surface a merge/release risk conclusion when residual risk remains.
6. When a solution, conclusion, conflict, or technical choice needs user judgment, prompt the user to decide and record the decision in the affected module's `vibe-coding-clarify.md`.

Only skip one of these steps when it is clearly not applicable, or when blocked by signing, private Maven credentials, device/emulator availability, environment ambiguity, or explicit user direction.
