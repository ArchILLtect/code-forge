INSERT INTO CHALLENGES (TITLE, DIFFICULTY, BLURB, PROMPT_MD)
VALUES
  ('Two Sum', 'EASY', 'Find two numbers adding up to target.', 'Given an int array nums and int target, return indices of the two numbers such that they add up to target.'),
  ('Valid Parentheses', 'EASY', 'Check if parentheses are balanced.', 'Given a string s containing ''()[]{}'', determine if the input string is valid.'),
  ('Merge Intervals', 'MEDIUM', 'Merge overlapping intervals.', 'Given an array of intervals where intervals[i] = [starti, endi], merge all overlapping intervals.'),
  ('Best Time to Buy and Sell Stock', 'EASY', 'Max profit from single stock trade.', 'Given an array prices where prices[i] is the price of a stock on day i, maximize profit.'),
  ('Binary Tree Level Order Traversal', 'MEDIUM', 'Traverse levels of a binary tree.', 'Given the root of a binary tree, return the level order traversal of its nodes'' values.'),
  ('LRU Cache', 'HARD', 'Design a least-recently-used cache.', 'Design a data structure that supports get and put with O(1) average time complexity.'),
  ('Longest Substring Without Repeating Characters', 'MEDIUM', 'Find longest unique-char substring.', 'Given a string s, find the length of the longest substring without repeating characters.'),
  ('Climbing Stairs', 'EASY', 'Count ways to climb stairs.', 'Given n, return how many distinct ways you can climb to the top.'),
  ('Number of Islands', 'MEDIUM', 'Count islands in a grid.', 'Given an m x n 2D binary grid, return the number of islands.'),
  ('Median of Two Sorted Arrays', 'HARD', 'Find median of two sorted arrays.', 'Given two sorted arrays nums1 and nums2, return the median of the two sorted arrays.');

-- Seed submissions for a few challenges (dev only)
INSERT INTO SUBMISSIONS (CHALLENGE_ID, OUTCOME, CODE)
VALUES
  (1, 'CORRECT', 'int[] twoSum(int[] nums, int target) { /* ... */ }'),
  (1, 'INCORRECT', '/* first attempt */'),
  (2, 'ACCEPTABLE', 'boolean isValid(String s) { /* ... */ }'),
  (3, 'SKIPPED', NULL);

-- Seed drill items for a few challenges (dev only)
INSERT INTO DRILL_ITEMS (CHALLENGE_ID, TIMES_SEEN, STREAK, NEXT_DUE_AT)
VALUES
  (1, 2, 1, NULL),
  (2, 1, 0, NULL),
  (3, 3, 2, NULL);