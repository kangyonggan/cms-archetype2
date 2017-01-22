<#assign ctx="${(rca.contextPath)!''}">
<#assign system = RequestParameters.system!'' />

<div class="page-header">
    <h1>
        缓存列表
        <small class="pull-right">
            <a href="${ctx}/dashboard/content/cache/clearall?system=${system}" id="clearall" class="btn btn-sm btn-danger">清空列表</a>
        </small>
    </h1>
</div>

<div class="space-10"></div>

<form class="form-inline" method="get" novalidate>
    <div class="form-group">
        <select name="system" class="form-control" style="min-width: 150px;">
        <#list systems as sys>
            <option value="${sys.code}" <#if system=='${sys.code}'>selected</#if>>${sys.value}</option>
        </#list>
        </select>
    </div>

    <button class="btn btn-sm btn-inverse" data-toggle="search-submit">
        搜索
        <span class="ace-icon fa fa-search icon-on-right bigger-110"></span>
    </button>
</form>

<div class="space-10"></div>

<table id="cache-table" class="table table-striped table-bordered table-hover">
    <thead>
    <tr>
        <th>系统</th>
        <th>键</th>
        <th>操作</th>
    </tr>
    </thead>
    <tbody>
    <#if keys?size gt 0>
        <#list keys as key>
            <#include "table-tr.ftl"/>
        </#list>
    <#else>
    <tr>
        <td colspan="20">
            <div class="empty">暂无查询记录</div>
        </td>
    </tr>
    </#if>
    </tbody>
</table>

<script src="${ctx}/static/app/js/dashboard/content/cache/list.js"></script>
