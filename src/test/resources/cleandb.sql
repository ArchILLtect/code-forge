-- Purpose: Reset schema and seed sample data for local/dev testing
-- PostgreSQL version

-- Drop tables (CASCADE handles foreign keys automatically)
DROP TABLE IF EXISTS submissions CASCADE;
DROP TABLE IF EXISTS drill_items CASCADE;
DROP TABLE IF EXISTS challenges CASCADE;

-- ------------------------------------------
-- Recreate tables
-- ------------------------------------------

CREATE TABLE challenges (
        id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
        title VARCHAR(255) NOT NULL,
        blurb TEXT,
        prompt_md TEXT,
        expected_answer TEXT,
        difficulty VARCHAR(20) NOT NULL CHECK (difficulty IN ('EASY', 'MEDIUM', 'HARD')),
        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
        updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
        CONSTRAINT uk_challenges_title UNIQUE (title)
);

CREATE TABLE drill_items (
         id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
         challenge_id BIGINT NOT NULL,
         user_id VARCHAR(64) NOT NULL, -- MVP: string-based user identifier (e.g., 'demo' or Cognito sub)
         times_seen INT NOT NULL DEFAULT 0,
         streak INT NOT NULL DEFAULT 0,
         created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
         updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
         next_due_at TIMESTAMP NULL DEFAULT NULL,
         version BIGINT NOT NULL DEFAULT 0,
         CONSTRAINT uk_drill_items_user_challenge UNIQUE (user_id, challenge_id),
         CONSTRAINT fk_drill_items_challenge
             FOREIGN KEY (challenge_id)
                 REFERENCES challenges(id)
                 ON DELETE CASCADE
                 ON UPDATE CASCADE
);

CREATE TABLE submissions (
         id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
         challenge_id BIGINT NOT NULL,
         user_id VARCHAR(64) NOT NULL, -- MVP: string-based user identifier (e.g., 'demo' or Cognito sub)
         outcome VARCHAR(20) NOT NULL CHECK (outcome IN ('CORRECT', 'INCORRECT', 'ACCEPTABLE', 'SKIPPED')),
         code TEXT,
         created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
         updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
         CONSTRAINT fk_submissions_challenge
             FOREIGN KEY (challenge_id)
                 REFERENCES challenges(id)
                 ON DELETE CASCADE
                 ON UPDATE CASCADE
);

-- Helpful index
CREATE INDEX idx_drill_items_user_due ON drill_items (user_id, next_due_at);
CREATE INDEX idx_submissions_challenge_id ON submissions (challenge_id);
CREATE INDEX idx_submissions_user_id ON submissions (user_id);

-- ------------------------------------------
-- Seed sample data
-- ------------------------------------------

INSERT INTO challenges (title, blurb, prompt_md, expected_answer, difficulty) VALUES
        ('Array Pivot', 'Find pivot index in an array', 'Prompt\nGiven an array...', 'pivotIndex', 'EASY'),
        ('Linked Reversal', 'Reverse a singly linked list', 'Prompt\nImplement reverse...', 'reverseList', 'EASY'),
        ('Two Sum', 'Find two numbers adding up to target', 'Given an int array nums and int target, return indices of the two numbers such that they add up to target.', 'twoSum', 'EASY'),
        ('Valid Parentheses', 'Check if parentheses are balanced.', 'Given a string s containing ‘()[]{}’, determine if the input string is valid.', 'isValid', 'EASY'),
        ('Best Time to Buy and Sell Stock', 'Max profit from single stock trade.', 'Given an array prices where prices[i] is the price of a stock on day i, maximize profit.', 'maxProfit', 'EASY'),
        ('Climbing Stairs', 'Count ways to climb stairs.', 'Given n, return how many distinct ways you can climb to the top.', 'climbStairs', 'EASY'),
        ('Balanced Brackets', 'Check bracket balance', 'Prompt\nUse stack...', 'isBalanced', 'MEDIUM'),
        ('Max Subarray', 'Kadane''s algorithm', 'Prompt\nFind max sum...', 'maxSubArray', 'MEDIUM'),
        ('Merge Intervals', 'Merge overlapping intervals.', 'Given an array of intervals where intervals[i] = [starti, endi], merge all overlapping intervals.', 'merge', 'MEDIUM'),
        ('Binary Tree Level Order Traversal', 'Traverse levels of a binary tree.', 'Given the root of a binary tree, return the level order traversal of its nodes’ values.', 'levelOrder', 'MEDIUM'),
        ('Longest Substring Without Repeating Characters', 'Find longest unique-char substring.', 'Given a string s, find the length of the longest substring without repeating characters.', 'lengthOfLongestSubstring', 'MEDIUM'),
        ('Number of Islands', 'Count islands in a grid.', 'Given an m x n 2D binary grid, return the number of islands.', 'numIslands', 'MEDIUM'),
        ('LRU Cache', 'Design a least-recently-used cache.', 'Design a data structure that supports get and put with O(1) average time complexity.', 'LRUCache', 'HARD'),
        ('Median of Two Sorted Arrays', 'Find median of two sorted arrays.', 'Given two sorted arrays nums1 and nums2, return the median of the two sorted arrays.', 'findMedianSortedArrays', 'HARD');

-- Drill items (assumes auto-increment IDs 1–6)
INSERT INTO drill_items (challenge_id, user_id, times_seen, streak, next_due_at) VALUES
        (1, 'demo', 1, 1, NULL),
        (2, 'demo', 0, 0, NULL),
        (3, 'demo', 2, 1, NULL),
        (4, 'demo', 2, 1, NULL),
        (5, 'demo', 1, 0, NULL),
        (6, 'demo', 3, 2, NULL);

-- Submissions
INSERT INTO submissions (challenge_id, user_id, outcome, code) VALUES
        (1, 'demo', 'CORRECT', 'int pivot = ...'),
        (1, 'demo', 'INCORRECT', '/* attempt */'),
        (3, 'demo', 'ACCEPTABLE', '/* near correct */'),
        (4, 'demo', 'CORRECT', '/* correct */'),
        (4, 'demo', 'INCORRECT', '/* attempt */'),
        (5, 'demo', 'ACCEPTABLE', '/* near correct */'),
        (6, 'demo', 'SKIPPED', '/* unfinished */');