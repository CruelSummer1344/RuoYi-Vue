use `ry-vue`;
-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('评论', '3', '1', 'comments', 'comments/comments/index', 1, 0, 'C', '0', '0', 'comments:comments:list', '#', 'admin', sysdate(), '', null, '评论菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('评论查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'comments:comments:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('评论新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'comments:comments:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('评论修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'comments:comments:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('评论删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'comments:comments:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('评论导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'comments:comments:export',       '#', 'admin', sysdate(), '', null, '');