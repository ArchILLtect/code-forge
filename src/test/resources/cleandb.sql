-- Purpose: Reset schema and seed sample data for local/dev testing
-- MySQL version

-- Disable FK checks for dropping
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS submissions;
DROP TABLE IF EXISTS drill_items;
DROP TABLE IF EXISTS challenges;

SET FOREIGN_KEY_CHECKS = 1;

-- ------------------------------------------
-- Recreate tables
-- ------------------------------------------

CREATE TABLE challenges (
                            id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                            title VARCHAR(255) NOT NULL,
                            blurb TEXT,
                            prompt_md TEXT,
                            difficulty ENUM('EASY', 'MEDIUM', 'HARD') NOT NULL,
                            created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                            UNIQUE KEY uk_challenges_title (title)
);

CREATE TABLE drill_items (
                             id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                             challenge_id BIGINT NOT NULL,
                             times_seen INT NOT NULL DEFAULT 0,
                             streak INT NOT NULL DEFAULT 0,
                             created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                             next_due_at TIMESTAMP NULL DEFAULT NULL,
                             version BIGINT NOT NULL DEFAULT 0,
                             UNIQUE KEY uk_drill_items_challenge_id (challenge_id),
                             CONSTRAINT fk_drill_items_challenge
                                 FOREIGN KEY (challenge_id)
                                     REFERENCES challenges(id)
                                     ON DELETE CASCADE
                                     ON UPDATE CASCADE
);

ALTER TABLE drill_items MODIFY version BIGINT NOT NULL DEFAULT 0;
UPDATE drill_items SET version = 0 WHERE version IS NULL;

CREATE TABLE submissions (
                             id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                             challenge_id BIGINT NOT NULL,
                             outcome ENUM('CORRECT', 'INCORRECT', 'ACCEPTABLE', 'SKIPPED') NOT NULL,
                             code TEXT,
                             created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                             CONSTRAINT fk_submissions_challenge
                                 FOREIGN KEY (challenge_id)
                                     REFERENCES challenges(id)
                                     ON DELETE CASCADE
                                     ON UPDATE CASCADE
);

-- Helpful index
CREATE INDEX idx_submissions_challenge_id ON submissions (challenge_id);

-- ------------------------------------------
-- Seed sample data
-- ------------------------------------------

INSERT INTO challenges (title, blurb, prompt_md, difficulty) VALUES
                                                                 ('Array Pivot', 'Find pivot index in an array', 'Prompt\nGiven an array...', 'EASY'),
                                                                 ('Linked Reversal', 'Reverse a singly linked list', 'Prompt\nImplement reverse...', 'EASY'),
                                                                 ('Two Sum', 'Find two numbers adding up to target', 'Given an int array nums and int target, return indices of the two numbers such that they add up to target.', 'EASY'),
                                                                 ('Valid Parentheses', 'Check if parentheses are balanced.', 'Given a string s containing ‘()[]{}’, determine if the input string is valid.', 'EASY'),
                                                                 ('Best Time to Buy and Sell Stock', 'Max profit from single stock trade.', 'Given an array prices where prices[i] is the price of a stock on day i, maximize profit.', 'EASY'),
                                                                 ('Climbing Stairs', 'Count ways to climb stairs.', 'Given n, return how many distinct ways you can climb to the top.', 'EASY'),
                                                                 ('Balanced Brackets', 'Check bracket balance', 'Prompt\nUse stack...', 'MEDIUM'),
                                                                 ('Max Subarray', 'Kadane''s algorithm', 'Prompt\nFind max sum...', 'MEDIUM'),
                                                                 ('Merge Intervals', 'Merge overlapping intervals.', 'Given an array of intervals where intervals[i] = [starti, endi], merge all overlapping intervals.', 'MEDIUM'),
                                                                 ('Binary Tree Level Order Traversal', 'Traverse levels of a binary tree.', 'Given the root of a binary tree, return the level order traversal of its nodes’ values.', 'MEDIUM'),
                                                                 ('Longest Substring Without Repeating Characters', 'Find longest unique-char substring.', 'Given a string s, find the length of the longest substring without repeating characters.', 'MEDIUM'),
                                                                 ('Number of Islands', 'Count islands in a grid.', 'Given an m x n 2D binary grid, return the number of islands.', 'MEDIUM'),
                                                                 ('LRU Cache', 'Design a least-recently-used cache.', 'Design a data structure that supports get and put with O(1) average time complexity.', 'HARD'),
                                                                 ('Median of Two Sorted Arrays', 'Find median of two sorted arrays.', 'Given two sorted arrays nums1 and nums2, return the median of the two sorted arrays.', 'HARD');

-- Drill items (assumes auto-increment IDs 1–4)
INSERT INTO drill_items (challenge_id, times_seen, streak, next_due_at) VALUES
                                                                            (1, 1, 1, NULL),
                                                                            (2, 0, 0, NULL),
                                                                            (3, 2, 1, NULL),
                                                                            (4, 2, 1, NULL),
                                                                            (5, 1, 0, NULL),
                                                                            (6, 3, 2, NULL);

-- Submissions
INSERT INTO submissions (challenge_id, outcome, code) VALUES
                                                          (1, 'CORRECT', 'int pivot = ...'),
                                                          (1, 'INCORRECT', '/* attempt */'),
                                                          (3, 'ACCEPTABLE', '/* near correct */'),
                                                          (4, 'CORRECT', '/* correct */'),
                                                          (4, 'INCORRECT', '/* attempt */'),
                                                          (5, 'ACCEPTABLE', '/* near correct */'),
                                                          (6, 'SKIPPED', '/* unfinished */');