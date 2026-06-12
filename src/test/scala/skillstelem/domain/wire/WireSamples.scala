package skillstelem.domain.wire

object WireSamples {
   object Claude {
      val ptuSample = """
        {
            "session_id": "abc123",
            "transcript_path": "/Users/.../.claude/projects/.../00893aaf-19fa-41d2-8238-13269b9b3ca0.jsonl",
            "cwd": "/Users/...",
            "permission_mode": "default",
            "hook_event_name": "PostToolUse",
            "tool_name": "Skill",
            "tool_input": {
                "skill": "review"
            },
            "tool_response": {
                "filePath": "/path/to/file.txt",
                "success": true
            },
            "tool_use_id": "toolu_01ABC123...",
            "duration_ms": 12
        }
        """

      val upeSample = """
        {
           "session_id": "abc123",
           "transcript_path": "/Users/.../00893aaf.jsonl",
           "cwd": "/Users/...",
           "permission_mode": "default",
           "hook_event_name": "UserPromptExpansion",
           "expansion_type": "slash_command",
           "command_name": "example-skill",
           "command_args": "arg1 arg2",
           "command_source": "plugin",
           "prompt": "/example-skill arg1 arg2"
        }
        """
   }

   object Cursor {
      val rawPtu = """
         {
            "conversation_id": "abc1234",
            "hook_event_name": "postToolUse",
            "tool_name": "Read",
            "tool_input": {
               "file_path": "/home/test"
            },
            "duration": 14.755
         }
        """

      val rawPtuSkill = """
         {
            "conversation_id": "abc1234",
            "hook_event_name": "postToolUse",
            "tool_name": "Read",
            "tool_input": {
               "file_path": "/home/user1/.cursor/skills/sample-skill/SKILL.md"
            },
            "duration": 14.755
         }
        """
   }
}
