use `ry-vue`;

-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('旅游项目', '3', '1', 'projects', 'projects/projects/index', 1, 0, 'C', '0', '0', 'projects:projects:list', '#', 'admin', sysdate(), '', null, '旅游项目菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('旅游项目查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'projects:projects:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('旅游项目新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'projects:projects:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('旅游项目修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'projects:projects:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('旅游项目删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'projects:projects:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('旅游项目导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'projects:projects:export',       '#', 'admin', sysdate(), '', null, '');