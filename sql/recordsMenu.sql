use `ry-vue`;
-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('考勤记录', '0', '1', 'records', 'records/records/index', 1, 0, 'C', '0', '0', 'records:records:list', '#', 'admin', sysdate(), '', null, '考勤记录菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('考勤记录查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'records:records:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('考勤记录新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'records:records:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('考勤记录修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'records:records:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('考勤记录删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'records:records:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('考勤记录导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'records:records:export',       '#', 'admin', sysdate(), '', null, '');