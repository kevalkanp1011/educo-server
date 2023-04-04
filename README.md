
# Educo Server

## Routes

## Auth API
1. SignUp User : /api/user/create
2. SignIn User: /api/user/login
3. Reset Password: /api/user/reset_password
4. Authenticate: /api/user/authenticate

| Route | Response |
| ------ | ------ |
| /api/user/create | BasicApiResponse<Unit> |
| /api/user/login | BasicApiResponse<AuthResponse> |
| /api/user/reset_password | BasicApiResponse<Unit> |
| /api/user/authenticate |  |


## Profile API
5. Update User: /api/user/update
6. Get Courses For Profile: /api/user/profile/courses
7. Get Authenticated User Profile: /api/user/profile
8. Profile Header: /api/user/profile_header
9. Update Profile: /api/user/profile/update
10. Search User: /api/user/search
11. Follow User: /api/user/follow
12. unFollow User: /api/user/unfollow
13. Get User Information: /api/get/users

## Course API
14. Search Courses: /api/user/search/courses
15. Bookmark Courses: /api/user/bookmark/courses
16. Create Bookmark: /api/user/bookmark/create
17. Get Popular Category: /api/course/popular_categories
18. Get Most Watched Courses: /api/course/most_watched_courses
19. Get Previous Watched Courses: /api/course/previous_watched_courses
20. Get Others Watched Courses: /api/course/others_watched_courses
21. Get Course Details: /api/course/details
22. Get Comments For Course: /api/user/course/comments
23. Get Projects For Course: /api/user/course/projects
24. Get Resources For Course: /api/user/course/resources
25. Get Lessons For Course: /api/user/course/lessons
26. Create a comment: /api/comment/create
27. Like a parent: /api/like
28. unlike a parent: /api/unlike
29. Get likes for a parent: /api/like/parent


