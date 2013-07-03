# Git Tasks for Ant

This project is a set of tasks dedicated to git automation with [Ant](http://ant.apache.org/), and it use the native java implementation [JGit](http://www.eclipse.org/jgit/) api to do the job.

The minimum dependencies are Java 5, Ant 1.8 and JGit 3.0.

## Usage

You can either put all the dependencies to your ant lib folder and use the short declaration:

```xml
<taskdef resource="org/oecd/ant/git/antlib.xml">
```

Or use the verbose but safest one:

```xml
<taskdef resource="org/oecd/ant/git/antlib.xml">
    <classpath>
    	<pathelement location="${basedir}\lib\org.oecd.ant.git.jar"/>
        <pathelement location="${basedir}\lib\org.eclipse.jgit_3.0.0.201306101825-r.jar"/>
        <pathelement location="${basedir}\lib\org.eclipse.jgit.java7_3.0.0.201306101825-r.jar"/>
        <pathelement location="${basedir}\lib\com.jcraft.jsch_0.1.46.v201205102330.jar"/>
    </classpath>
</taskdef>
```

Note that `com.jcraft.jsch` is only mandatory if you plan to use the clone/pull/push tasks.
`org.eclipse.jgit.java7` is optionnal, it will be loaded dynamicaly if you are using a java 7 jvm and will help a bit on performances.

## Common
### Attributes

`repo`

The repository to open. May be either the GIT_DIR, or the working tree directory that contains .git. (**mandatory**)

`if`

Perform the task execution if the defined property has been set

`unless`

Prevent the task execution if the defined property has been set

## Add task (`gitadd`)

Add file contents to the index.

### Attributes

`update`

If set to `true`, the command only matches filepattern against already tracked files in the index rather than the working tree. That means that it will never stage new files, but that it will stage modified new contents of tracked files and that it will remove files from the index if the corresponding files in the working tree have been removed. In contrast to the git command line a filepattern must exist also if update is set to true as there is no concept of a working directory here. Default `false`.

`filepatterns`

A list of space or comma separated files to add content from. Also a leading directory name (e.g. `dir` to add `dir/file1` and `dir/file2`) can be given to add all files in the directory, recursively. Fileglobs (e.g. `*.c`) are not yet supported. Default `.`.

## Clone task (`gitclone`)

Clone a repository into a new directory.

### Attributes

`uri`

The uri to clone from. (**mandatory**)

`bare`

Whether the cloned repository is bare or not. Default `false`.

`branch`

The initial branch to check out when cloning the repository. Can be specified as ref name (refs/heads/master), branch name (master) or tag name (v1.2.3). Default `master`.

### Nested elements

#### credentials (max 1)

`username`

`password`

### Sample

```xml
<gitclone repo="${my.repo}" uri="https://github.com/j.doe/myproject.git">
  <credentials username="j.doe" password="secret"/>
</gitclone>
```

## Commit task (`gitcommit`)

Record changes to the repository.

### Attributes

`all`

If set to true the Commit command automatically stages files that have been modified and deleted, but new files not known by the repository are not affected.
This corresponds to the parameter -a on the command line.
Default `false`.

`amend`

Used to amend the tip of the current branch. If set to true, the previous commit will be amended.
Default `false`.

`message`

The commit message used for the commit.

`revlength`

The commit revision sha1 length to set in `revproperty`.

`revproperty`

A property to set with the commit revision sha1. Truncated to `revlength` if setted.

### Nested elements

#### author (max 1)

`name`

`email`

#### committer (max 1)

`name`

`email`

## Config task (`gitconfig`)

Get and set repository options.

### Attributes

Same attributes as nested option elements.

### Nested elements

#### option (max n)

`section`

The section (eg. `branch` in `[branch "devel"]` or `core` in ... `[core]`). (**mandatory**).

`subsection`

The subsection (eg. `devel` in `[branch "devel"]`).

`name`

The key name (eg. `remote`). (**mandatory**).

`value`

The value to set for this option. If blank, the option will be removed.

`property`

The property to set with the current value of the option.

## Diff task (`gitdiff`)

Show changes between commits, commit and working tree, etc.

### Attributes

`cached`

Whether to view the changes you staged for the next commit, it will generate a diff between `HEAD` (if `oldrev` undefined) and the index.
If `true`, `newrev` can't be defined.
Default `false`.

`newrev`

The target commit revision. If undefined, and `cached` set to `false`, the diff upper state will be the working tree.

`oldrev`

The old commit revision. If undefined, and `cached` set to `false`, the diff lower state will be the index, if `cached` set to `true`, it is `HEAD`.

`filter`

Limit the diff to the named path.

Path strings are relative to the root of the repository. If the user's input should be assumed relative to a subdirectory of the repository the caller must prepend the subdirectory's path prior to creating the filter. 
Path strings use '/' to delimit directories on all platforms.
All trailing '/' characters will be trimmed before string's length is checked or is used as part of the constructed filter.

`output`

The file to write the diff stream. If undefined, it will be more like a git diff --name-status, which is more efficient if you don't need the diff content.

`added`

The id reference for the resources collection to create with the list of added files.

`changed`

The id reference for the resources collection to create with the list of changed files.

`removed`

The id reference for the resources collection to create with the list of removed files.

## Extract task (`gitextract`)

Extract file(s) from any revision.

### Attributes

`rev`

The revision from which the file(s) will be extracted.
Default `HEAD`.

`srcfile`

The file to be extracted, relative to repository root directory.

`dstfile`

The destination file. Only one of `dstfile` and `dstdir` may be set.

`dstdir`

The destination directory. Only one of `dstfile` and `dstdir` may be set.

### Nested elements

#### fileset or any other resource collection

[Resource Collections](http://ant.apache.org/manual/Types/resources.html#collection) are used to select groups of files to extract. To use a resource collection, the `dstdir` attribute must be set.

#### mapper

You can define filename transformations by using a nested [mapper](http://ant.apache.org/manual/Types/mapper.html) element.

Note that the source name handed to the mapper depends on the resource collection you use. If you use <fileset> or any other collection that provides a base directory, the name passed to the mapper will be a relative filename, relative to the base directory. In any other case the absolute filename of the source will be used.

## Push task (`gitpush`)

Update remote refs along with associated objects.

### Attributes

`remote`

The remote (uri or name) used for the push operation.
Default `origin`.

`refSpecs`

The ref specs to be used in the push operation. Default is push refspecs defined in repository configuration, or current branch.

`all`

Whether to push all branches under refs/heads/*.
Default `false`.

`dryRun`

Sets whether the push operation should be a dry run.
Default `false`.

`force`

Sets the force preference for push operation.
Default `false`.

`receivePack`

The remote executable providing receive-pack service for pack transports. If no receive-pack is set, the default value of `git-receive-pack` will be used.

`tags`

Whether to push all tags under refs/tags/*. Default `false`.

`thin`

Sets the thin-pack preference for push operation.
Default `false`.

`timeout`

The timeout used for the transport step. No limit by default.

### Nested elements

#### credentials (max 1)

`username`

`password`

## Ref task (`gitref`)

Extract the commit revision for a reference.

### Attributes

`name`

The name of the ref to lookup. May be a short-hand form, e.g. "master" which is is automatically expanded to "refs/heads/master" if "refs/heads/master" already exists.
(**mandatory**)

`property`

A property to set with the commit revision sha1. Truncated to `length` if setted.

`length`

The commit revision sha1 length to set in `property`.

## Status task (`gitstatus`)

Show the working tree status. 

### Attributes

`isclean`

Property to set to `true` if no differences exist between the working-tree, the index, and the current HEAD.

`isdirty`

Property to set to `true` if differences exist between the working-tree, the index, or the current HEAD.

`untracked`

The id reference for the resources collection to create with the list of untracked files.
Files that are not ignored, and not in the index. (e.g. what you get if you create a new file without adding it to the index).

`added`

The id reference for the resources collection to create with the list of added files.
Files added to the index, not in HEAD (e.g. what you get if you call 'git add ...' on a newly created file).

`modified`

The id reference for the resources collection to create with the list of modified files.
Files modified on disk relative to the index (e.g. what you get if you modify an existing file without adding it to the index).

`changed`

The id reference for the resources collection to create with the list of changed files.
Files changed from HEAD to index (e.g. what you get if you modify an existing file and call 'git add ...' on it).

`missing`

The id reference for the resources collection to create with the list of missing files.
Files in index, but not filesystem (e.g. what you get if you call 'rm ...' on a existing file).

`removed`

The id reference for the resources collection to create with the list of removed files.
Files removed from index, but in HEAD (e.g. what you get if you call 'git rm ...' on a existing file).

## Git container task (`git`)

This task can group a sequence of git tasks avoiding the need of the repo attribute on each of them.

### Sample

```xml
<git repo="${my.repo}">
    <gitref name="HEAD" property="head.sha1"/>
    <echo message="${head.sha1}"/>
    <gitstatus isclean="repo.isclean"/>
    <echo message="${repo.isclean}"/>
</git>
```