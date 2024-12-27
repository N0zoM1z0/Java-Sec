#include <stdio.h>
#include <stdlib.h>
#include <stdarg.h>
#include <unistd.h>
#include <sys/stat.h>
#include <dirent.h>
#include <dlfcn.h>
#include <string.h>

#include <string>
#include <vector>
#include <map>
#include <fstream>

#ifndef _WIN32 
#define MAX_PATH 260
#define _vscprintf(f,a) vsnprintf(NULL,0,f,a)
#define vsprintf_s(s,n,f,a) vsnprintf(s,n,f,a)
#endif 

#ifndef _WIN32 
int _memicmp(const void* first, const void* last, size_t count)
{
    int f = 0, l = 0;
    const char* dst = (const char*)first, *src = (const char*)last;
    while(count-- && f == l)
    {
        f = toupper(*(dst++));
        l = toupper(*(src++));
    }
    return (f - l);
}

int GetModuleFileNameA(void *hModule, char *name, int size)
{
    int count = 0;
    count = readlink("/proc/self/exe", name, size);
    name[count] = '\0';
    return strlen(name);
}
#endif 

typedef void (*decryptfunc)(const char *str, int inLen, const char *publicKey, int KeyLen, char **outStr, int *outLen);

template<typename T>
class array_ptr
{
    private:
        T* m_data;
        
        array_ptr();
        
    public:
        array_ptr(T* data) : m_data(data) {}
          
        ~array_ptr()
        {
            try
            {
                if(m_data)
                    delete[] m_data;
            }
            catch(...)
            {
            }
        }
        
        inline T* operator->() const {return m_data;}
        inline T* get() {return m_data;}
        inline T* release()
        {
            T* data = m_data;
            m_data = NULL;
            return data;
        }
};

inline const char* GetOffsetBin(const char* data, size_t offset)
{
  return (data == NULL) ? NULL : data + ((offset <= 0) ? strlen(data) : offset);
}

bool IsEmpty(const char* data)
{
    return (data == NULL) || (*data == '\0');
}

std::string Format(const char* fmt, ...)
{
    if((fmt == NULL) || (*fmt == '\0'))
        return "";

    va_list ap;
    va_start(ap, fmt);
    
    size_t size = 2048;
    
    array_ptr<char> ret_h(new char[size]);
    char* ret = ret_h.get();
    if(ret == NULL)
    {
        va_end(ap);
        return "";
    }

    int len = vsprintf_s(ret, size, fmt, ap);
    va_end(ap);
    
    if(len <= 0)
      return "";
      
    if(static_cast<size_t>(len) >= size)
    {
        va_list ap1;
        va_start(ap1, fmt);
        
        size = len + 1;
        
        array_ptr<char> ret1_h(new char[size]);
        ret = ret1_h.get();
        if(ret == NULL)
        {
            va_end(ap1);
            return "";
        }
        
        len = vsprintf_s(ret, size, fmt, ap1);
        va_end(ap1);
        
        if(len <= 0)
          return "";
    }

    ret[len] = '\0';
    return ret;
}

std::string ReadFile(const char* path)
{
    if(path == NULL)
        return "";

    try
    {
        std::ifstream inFile(path, std::ios_base::in | std::ios_base::binary);
        if(!inFile.is_open())
            return "";

        inFile.seekg(0, std::ios::end);
        size_t fsize = inFile.tellg();

        if(fsize <= 0)
        {
            inFile.close();
            return "";
        }

        inFile.seekg(0, std::ios::beg);

        array_ptr<char> buffer_h(new char[fsize + 1]);
        char* buf = buffer_h.get();

        if(buf == NULL)
        {
            inFile.close();
            return "";
        }

        memset(buf, 0, fsize + 1);
        inFile.read(buf, static_cast<std::streamsize>(fsize + 1));
        inFile.close();

        return std::string(buf, fsize);
    }
    catch(...)
    {
    }

    return "";
}

bool WriteFile(const char* path, const char* data, size_t len = 0, bool clear = false)
{
    if((path == NULL) || (data == NULL))
        return false;

    try
    {
        if(len <= 0)
            len = strlen(data);

        std::ofstream ofile(path, clear ? (std::ios_base::out | std::ios_base::binary) :
            (std::ios_base::out | std::ios_base::binary | std::ios_base::app));

        if(!ofile.is_open())
            return false;

        ofile.write(data, len);
        ofile.close();

        return true;
    }
    catch(...)
    {
    }

    return false;
}

const char* SkipChar(const char* &ptr, const char* skip, size_t len= 0)
{
    if((ptr == NULL) || (skip == NULL))
        return ptr;

    const char* end = ptr + ((len > 0) ? len : strlen(ptr));

    while((ptr < end) && ((*ptr == '\0') || (strchr(skip, *ptr) != NULL)))
        ptr++;

    return ptr;
}

const char* MoveToChar(const char* &ptr, const char* target, size_t len = 0)
{
    if((ptr == NULL) || (target == NULL) || (*target == '\0'))
        return ptr;

    const char* end = ptr + ((len > 0) ? len : strlen(ptr));

    while((ptr < end) && ((*ptr == '\0') || (strchr(target, *ptr) == NULL)))
        ptr++;

    return ptr;
}

bool IsAbsolutePath(const char* path)
{
    if (IsEmpty(path))
        return false;

    size_t len = strlen(path);

    if (len >= 1)
    {
        /*以字符'/'开头被视为绝对路径*/
        if ((path[0] == '/') || (path[0] == '\\'))
            return true;
    }

    if (len >= 2)
    {
        /*开头是大小写字母，后面跟随一个冒号':'，就被视为绝对路径*/
        if ((((path[0] >= 'a') && (path[0] <= 'z')) || ((path[0] >= 'A') && (path[0] <= 'Z'))) && (path[1] == ':'))
        {
            if ((len >= 3) && ((path[2] == '\\') || (path[2] == '/')))
                return true;

            if (len < 3)
                return true;

            return false;
        }
    }

    return false;
}

std::string MakeRegularPath(const char* path)
{
    if (IsEmpty(path))
        return "";

    bool abspath = IsAbsolutePath(path);    /*记录是否绝对路径*/
    char splitter = '\0';                   /*用于记录分隔符*/

    std::string spath = path;
    size_t save = 0;
    size_t start = 0;

    while (start < spath.length())
    {
        size_t pos = spath.find_first_of("\\/", start);

        if (pos != std::string::npos)
        {
            if (splitter == '\0')
                splitter = spath[pos];
            else if (spath[pos] != splitter)
                spath.replace(pos, 1, 1, splitter);
        }
        else
        {
            pos = spath.length();
        }

        if (pos <= start)
        {
            if (pos < start)
                return "";      /*pos不可能小于start，如果出现，返回失败*/

            if (start <= 0)     /*路径起始位置是'\\'或'/'*/
            {
                save = ++start;
                continue;
            }

            /*遇到2个连续的分隔符，删掉后面一个*/
            spath.erase(start, 1);
            continue;
        }

        /*如果遇到一个"./"，则删除*/
        if ((start + 1 == pos) && (spath[start] == '.'))
        {
            spath.erase(start, pos - start + 1);

            if (start <= 0)
            {
                /*避免类似“.///”这样的字符串被解析成“/”开头的绝对路径*/
                while ((start < spath.length()) && ((spath[start] == '\\') || (spath[start] == '/')))
                {
                    spath.erase(start, 1);
                }
            }

            continue;
        }

        /*遇到一个"../"*/
        if ((start + 2 == pos) && (spath[start] == '.') && (spath[start + 1] == '.'))
        {
            if (abspath)    /*如果是绝对路径*/
            {
                /*如果上一级是根目录，则路径不合法*/
                if (save <= 0)
                    return "";

                /*如果出现start <= save，则路径不合法*/
                if (start <= save)
                    return "";
            }
            else            /*如果是相对路径*/
            {
                if (start <= save)
                {
                    /*如果start前面没有可以和"../"抵消的目录，则不删除*/
                    save = start = pos + 1;
                    continue;
                }
            }

            /*存在上一级目录，并且不是"../"， 也不是根目录，连同上级目录一起删除*/
            spath.erase(save, pos - save + 1);
            start = save;

            /*相对路径抵消所有上级目录后，start回到路径的起始位置*/
            if (start == 0)
            {
                /*避免类似“xxx/..///”这样的字符串被解析成“/”开头的绝对路径*/
                while ((start < spath.length()) && ((spath[start] == '\\') || (spath[start] == '/')))
                {
                    spath.erase(start, 1);
                }

                continue;
            }

            /*找到上一个可以用于抵消"../"的位置*/
            save = spath.find_last_of("\\/", start - 1);

            /*start的前面不是一个路径分隔符，出现错误*/
            if ((save == std::string::npos) || (save + 1 != start))
                return "";

            /*已经是起始位置，没有可以抵消的上级目录*/
            if (save <= 0)
            {
                save = start;
                continue;
            }

            save = spath.find_last_of("\\/", save - 1);
            if (save == std::string::npos)
                save = 0;
            else
                save++;

            /*如果start的前一级目录是"../"，则save=start*/
            if ((save + 3 == start) && (spath[save] == '.') && (spath[save + 1] == '.'))
                save = start;

            continue;
        }

        save = start;
        start = pos + 1;
    }

    if (spath.empty())
    {
        if (splitter == '\0')
            splitter = '/';

        spath += '.';
        spath += splitter;
    }

    return spath;
}

std::string GetFileName(const char* path)
{
    std::string spath = path ? path : "";
    size_t pos = spath.find_last_of("/\\");
    if (pos == std::string::npos)
        return spath;

    return spath.substr(pos + 1);
}

std::string CombinePath(const char* base, const char* sub)
{
    /*如果base或sub其中有一个是空字符串，则不合并*/
    if (IsEmpty(base) || IsEmpty(sub))
    {
        if (IsEmpty(sub))
            return MakeRegularPath(base);

        if (IsEmpty(base))
            return MakeRegularPath(sub);
    }

    /*如果sub是绝对路径，则不合并*/
    if (IsAbsolutePath(sub))
        return MakeRegularPath(sub);

    /*拼接base和sub，拼接后的路径可能包含"./"和"../"*/
    std::string sbase = base;

    size_t pos = sbase.find_last_of("\\/");     /*判断base和sub之间应该使用的分隔符*/
    if (pos == std::string::npos)
    {
        const char* pos = sub;
        MoveToChar(pos, "\\/");
        sbase += (*pos == '\\') ? "\\" : "/";
    }
    else
    {
        if (pos + 1 < sbase.length())
            sbase += sbase[pos];
    }

    sbase += sub;
    return MakeRegularPath(sbase.c_str());
}
std::string GetProcessPath(const char* subpath = NULL)
{
    {
        try
        {
            char sbuf[MAX_PATH + 1];
            memset(sbuf, 0, sizeof(sbuf));

            size_t length = GetModuleFileNameA(NULL, sbuf, sizeof(sbuf));
            if ((length <= 0) || (length >= sizeof(sbuf)))
                return "";

            std::string ret = sbuf;
            if (ret.empty())
                return "";

            size_t pos = ret.find_last_of("\\/");
            if (pos == std::string::npos)
                return "";

            ret.erase(pos + 1);
            if (IsEmpty(subpath))
                return ret;

            return CombinePath(ret.c_str(), subpath);
        }
        catch (...)
        {
        }
    }
    
    return "";
}

void ConvertKey(std::string& skey)
{
  for(size_t i=0; i<skey.length(); i++)
  {
    if(skey[i] == 46)
      skey[i] = 47;
  }
}

bool IsExist(const char* path)
{
#ifndef _WIN32
    if(access(path, F_OK) == 0)
        return true;
#endif

    return false;
}

bool IsFile(const char* path)
{
    struct stat info;
    stat(path,&info);
    if(S_ISREG(info.st_mode))
        return true;
     
    return false;
}

bool IsFolder(const char* path)
{
    struct stat info;
    stat(path,&info);
    if(S_ISDIR(info.st_mode))
        return true;
     
    return false;
}

bool RemoveFolder(const char* path)
{
  std::string scmd = Format("rm -fr %s", path);
  int ret = system(scmd.c_str());
  if(IsExist(path))
  {
    return false;
  }
  
  return true;
}

bool RemoveFile(const char* path)
{
    if(remove(path) == 0)
        return true;
        
    return false;
}

bool ListAllFiles(const char* path, std::vector<std::string>& files, bool recurive = false)
{
    DIR *dir;
    struct dirent *ptr;
    char base[1000];

    if ((dir=opendir(path)) == NULL)
        return false;
        
    while ((ptr=readdir(dir)) != NULL)
    {
        if(strcmp(ptr->d_name,".")==0 || strcmp(ptr->d_name,"..")==0)
            continue;
            
        std::string spath = Format("%s/%s", path, ptr->d_name);
        spath = MakeRegularPath(spath.c_str());
        files.push_back(spath);
        
        if(recurive && IsFolder(spath.c_str()))
        {
            ListAllFiles(spath.c_str(), files, recurive);
        }
    }
    
    closedir(dir);
    return true;
}

bool StringEqual(const char* strl, size_t len, const char* strr, bool icase = false)
{
    if((strl == NULL) || (strr == NULL))
        return false;

    if(len <= 0)
        len = strlen(strl);

    if(icase)
    {
        char chl = '\0', chr = '\0';

        while((len > 0) && *strr && (chl == chr))
        {
            chl = *strl++;
            chr = *strr++;
            len--;

            if((chl >= 'A') && chl <= 'Z')
                chl += 0x20;
            if((chr >= 'A') && chr <= 'Z')
                chr += 0x20;
        }

        return ((len == 0) && (*strr == '\0') && (chl == chr));
    }

    for(; (len > 0) && *strr && (*strl == *strr); strl++, strr++, len--)
        ; // do nothing

    return (len == 0) && (*strr == '\0');
}

bool StringEqual(const char* strl, const char* strr, bool icase = false)
{
    return StringEqual(strl, 0, strr, icase);
}

bool EndWith(const char* start, const char* sub, bool icase)
{
    if ((start == NULL) || (sub == NULL))
        return false;

    size_t slen = strlen(start);
    size_t sublen = strlen(sub);

    if ((sublen <= 0) || (slen < sublen))
        return false;

    start += slen - sublen;
    return StringEqual(start, sub, icase);
}

const char* StringFind(const char* start, size_t len, const char* sub, bool icase)
{
    if ((start == NULL) || (sub == NULL))
        return NULL;

    size_t sublen = strlen(sub);
    if (sublen <= 0)
        return NULL;

    const char* end = GetOffsetBin(start, len) - sublen;

    if (icase)
    {
        while (start <= end)
        {
            if (_memicmp(start, sub, sublen) == 0)
                return start;

            start++;
        }
    }
    else
    {
        while (start <= end)
        {
            if (memcmp(start, sub, sublen) == 0)
                return start;

            start++;
        }
    }

    return NULL;
}

std::string StringReplace(const char* start, size_t len, const char* sub,
    const char* target, bool icase)
{
    const char* search = StringFind(start, len, sub, icase);
    if (search == NULL)
    {
        if (IsEmpty(start))
            return "";

        return (len <= 0) ? start : std::string(start, len);
    }

    if (len <= 0)
        len = strlen(start);

    std::string ret = "";
    size_t sublen = strlen(sub);
    const char* end = start + len;

    while ((start + sublen <= end) && (search != NULL))
    {
        if (search > start)
            ret.append(start, search - start);

        ret += target ? target : "";

        start = search + sublen;
        len = end - start;

        /*由于StringFind可以查找start中'\0'后面的文本，因此'\0'后面的文本也可能被替换*/
        search = StringFind(start, len, sub, icase);
    }

    if (start < end)
        ret.append(start, end - start);

    return ret;
}

std::string StringReplace(const char* start, const char* sub, const char* data, bool icase = false)
{
    return StringReplace(start, 0, sub, data, icase);
}

std::string GetRelativePath(const char* base, const char* sub, bool icase)
{
    if (IsEmpty(base) || IsEmpty(sub))
        return sub ? sub : "";

    /*有一个不是绝对路径*/
    if (!IsAbsolutePath(base) || !IsAbsolutePath(sub))
        return sub;
    
    const char* basestart = base;
    size_t baselen = strlen(base);
    
    const char* substart = sub;
    size_t sublen = strlen(sub);

    while ((basestart < base + baselen) && (substart < sub + sublen))
    {
        const char* basepos = basestart;
        const char* subpos = substart;

        MoveToChar(basepos, "\\/");
        MoveToChar(subpos, "\\/");

        size_t dirlen = subpos - substart;

        if (dirlen != (basepos - basestart))
        {
            /*起始位置，盘符不一样，直接返回sub*/
            if ((basestart <= base) || (substart <= sub))
                return sub ? sub : "";

            /*从当前层级开始不一致*/
            break;
        }

        if (dirlen > 0)
        {
            bool eq = icase ? (_memicmp(basestart, substart, dirlen) == 0) :
                (memcmp(basestart, substart, dirlen) == 0);

            if (!eq)
            {
                /*起始位置，盘符不一样，直接返回sub*/
                if ((basestart <= base) || (substart <= sub))
                    return sub ? sub : "";

                /*从当前层级开始不一致*/
                break;
            }
        }

        basestart = basepos;
        substart = subpos;

        if (basestart >= base + baselen)
        {
            if (substart < sub + sublen)
                SkipChar(substart, "\\/");

            break;
        }

        if (substart >= sub + sublen)
            break;

        SkipChar(basestart, "\\/");
        SkipChar(substart, "\\/");
    }
    
    std::string sret = "";
    std::string splitter = "";
    while (basestart < base + baselen)
    {
        MoveToChar(basestart, "\\/");
        if (basestart >= base + baselen)
            break;

        if (splitter.empty())
            splitter.append(basestart, 1);

        sret += ".." + splitter;
        SkipChar(basestart, "\\/");
    }

    if (substart < sub + sublen)
    {
        if (splitter.empty())
        {
            const char* subpos = substart;
            MoveToChar(subpos, "\\/");
            if (subpos < sub + sublen)
                splitter.append(subpos, 1);

            /*如果此时splitter.empty()，这种情况下返回值也不可能包含分隔符*/
        }

        std::string replaced = (splitter[0] == '/') ? "\\" : "/";

        sret.append(StringReplace(substart, replaced.c_str(), splitter.c_str()));
    }

    if (sret.empty())
        sret = ".";

    return sret;
}

bool DecryptClass(decryptfunc ffunc, const std::string& spath, std::string& relativePath)
{
  if(!EndWith(spath.c_str(), ".class", true))
    return true;
    
  std::string sdata = ReadFile(spath.c_str());
  if(sdata.empty())
    return true;
    
  if(sdata.find_first_not_of("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789+/=") != std::string::npos)
    return true;
    
  int outlen = sdata.length() + 1;
  array_ptr<char> data_h(new char[outlen]);
  char* pdata = data_h.get();
  
  memset(pdata, 0, outlen);
  
  const char* start = StringFind(relativePath.c_str(), relativePath.length(), ".class", true);
  if(start != NULL)
  {
    relativePath.erase(static_cast<size_t>(start - relativePath.c_str()));
  }
  
  std::string skey = StringReplace(relativePath.c_str(), relativePath.length(), "/", ".", true);
  printf("file: %s, key: %s\n", spath.c_str(), skey.c_str());
  
  ConvertKey(skey);
  ffunc(sdata.c_str(), sdata.length(), skey.c_str(), skey.length(), &pdata, &outlen);
  
  if(outlen <= 0)
    return true;
    
  if(!WriteFile(spath.c_str(), pdata, outlen, true))
  {
    printf("failed to update class file: %s\n", spath.c_str());
    return false;
  }
  
  return true;
}

bool DecryptJar(decryptfunc ffunc, const std::string& spath)
{
  std::string stmp = GetProcessPath("temp/");
  if(IsExist(stmp.c_str()))
  {
    if(!RemoveFolder(stmp.c_str()) || IsExist(stmp.c_str()))
    {
      printf("failed to remove file or path: %s\n", stmp.c_str());
      return false;
    }
  }
  
  std::string scmd = Format("unzip -qq %s -d %s", spath.c_str(), stmp.c_str());
  int ret = system(scmd.c_str());
  if(!IsExist(stmp.c_str()))
  {
    printf("failed to unzip jar file: %s\n", spath.c_str());
    return false;
  }
  
  std::vector<std::string> files;
  ListAllFiles(stmp.c_str(), files, true);
  
  for(size_t i=0; i<files.size(); i++)
  {
    if(!IsFile(files[i].c_str()))
    {
      continue;
    }
    
    std::string relativePath = GetRelativePath(stmp.c_str(), files[i].c_str(), true);
    
    if(!DecryptClass(ffunc, files[i], relativePath))
    {
      printf("error: failed to decrypt class file: %s\n", files[i].c_str());
      return false;
    }
  }
  
  if(!RemoveFile(spath.c_str()) || IsExist(spath.c_str()))
  {
    printf("failed to remove class file: %s\n", spath.c_str());
    return false;
  }
  
  std::string scmd_jar = Format("jar cvf %s -C temp . 1>log.dat 2>&1", spath.c_str());
  ret = system(scmd_jar.c_str());
  if(!IsExist(spath.c_str()))
  {
    printf("fail to make jar file: %s\n", spath.c_str());
    return false;
  }
  
  stmp.erase(stmp.length() - 1);
  
  if(!RemoveFolder(stmp.c_str()) || IsExist(stmp.c_str()))
  {
    printf("failed to remove temp path: %s\n", stmp.c_str());
    return false;
  }
  
  return true;
}

int main(int argc, char* argv[])
{
  if(argc < 2)
  {
    printf("not enough arguments! usage:\n\t%s <path>\n", argv[0]);
    return 0;
  }
  
  std::string spath = GetProcessPath(argv[1]);
  
  printf("%s: listing jars.\n", argv[0]);
  std::vector<std::string> jars;
  ListAllFiles(spath.c_str(), jars, true);
  if(jars.empty())
  {
    printf("error: no jar files in path %s\n", spath.c_str());
    return 0;
  }
  
  printf("%s: loading library.\n", argv[0]);
  
  void *libm_handle = dlopen("javaCrypto.so", RTLD_LAZY );
  char *errorInfo = dlerror();
  if (errorInfo != NULL)
  {
    printf("error: dlopen failed: %s\n", errorInfo);
    return 0;
  }
  
  if (libm_handle == NULL)
  {
    printf("error: failed to load library: javaCrypto.so!\n");
    return 0;
  }
  
  decryptfunc ffunc = (decryptfunc)dlsym(libm_handle,"decryptWithPublicKey");
  errorInfo = dlerror();
  if (errorInfo != NULL)
  {
    dlclose(libm_handle);
    printf("error: dlsym failed: %s.\n", errorInfo);
    return 0;
  }
  
  if(ffunc == NULL)

  {
    dlclose(libm_handle);
    printf("error: dlsym failed: function is null.\n");
    return 0;
  }
  
  printf("%s: decripting jar files.\n", argv[0]);
  
  for(size_t i=0; i<jars.size(); i++)
  {
    if(!IsFile(jars[i].c_str()))
    {
      continue;
    }
    
    if(!EndWith(jars[i].c_str(), ".jar", true))
      continue;
    
    if(!DecryptJar(ffunc, jars[i]))
    {
      printf("error: failed to decrypt jar file: %s\n", jars[i].c_str());
      return 0;
    }
  }
  
  dlclose(libm_handle);
  
  printf("%s: done.\n", argv[0]);
  return 0;
}
