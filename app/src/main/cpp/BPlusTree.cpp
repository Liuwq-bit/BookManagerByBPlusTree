//
// Created by A1029 on 2022/4/2.
//

#include "BPlusTree.h"
#include <jni.h>
#include <fstream>

std::string JStrToStr(JNIEnv *env, jstring jStr) {
    if (!jStr)
        return "";

    const jclass stringClass = env->GetObjectClass(jStr);
    const jmethodID getBytes = env->GetMethodID(stringClass, "getBytes", "(Ljava/lang/String;)[B");
    const jbyteArray stringJbytes = (jbyteArray) env->CallObjectMethod(jStr, getBytes, env->NewStringUTF("UTF-8"));

    size_t length = (size_t) env->GetArrayLength(stringJbytes);
    jbyte* pBytes = env->GetByteArrayElements(stringJbytes, NULL);

    std::string ret = std::string((char *)pBytes, length);
    env->ReleaseByteArrayElements(stringJbytes, pBytes, JNI_ABORT);

    env->DeleteLocalRef(stringJbytes);
    env->DeleteLocalRef(stringClass);
    return ret;
}

/**
 * 图书部分
 */
extern "C"
JNIEXPORT jlong JNICALL
Java_com_example_bookmanagerbybplustree_BookBPlusTree_createNativeObject(JNIEnv *env, jobject thiz) {
    return (jlong) new BPlusTree<int, Book>();
}


extern "C"
JNIEXPORT void JNICALL
Java_com_example_bookmanagerbybplustree_BookBPlusTree_insertBook(JNIEnv *env, jobject thiz,
                                                                 jlong addr, jint index, jint id,
                                                                 jint available, jint total,
                                                                 jstring book_name,
                                                                 jstring book_info, jstring author,
                                                                 jstring author_info, jstring url) {

    ((BPlusTree<int, Book>*)addr)->insert(index, {id, available, total,
                                                  JStrToStr(env, book_name),
                                                  JStrToStr(env, book_info),
                                                  JStrToStr(env, author),
                                                  JStrToStr(env, author_info),
                                                  JStrToStr(env, url)});

}


extern "C"
JNIEXPORT jint JNICALL
Java_com_example_bookmanagerbybplustree_BookBPlusTree_findId(JNIEnv *env, jobject thiz, jlong addr,
                                                             jint index) {
    return ((BPlusTree<int, Book>*)addr)->find(index).id;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_example_bookmanagerbybplustree_BookBPlusTree_findAvailable(JNIEnv *env, jobject thiz,
                                                                    jlong addr, jint index) {
    return ((BPlusTree<int, Book>*)addr)->find(index).available;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_example_bookmanagerbybplustree_BookBPlusTree_findTotal(JNIEnv *env, jobject thiz,
                                                                jlong addr, jint index) {
    return ((BPlusTree<int, Book>*)addr)->find(index).total;
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_bookmanagerbybplustree_BookBPlusTree_findBookName(JNIEnv *env, jobject thiz,
                                                                   jlong addr, jint index) {
//    return toJstring(env, ((BPlusTree<int, Book>*)addr)->find(index).bookName);
//    string str = ((BPlusTree<int, Book>*)addr)->find(index).bookName.c_str();
    return env->NewStringUTF(((BPlusTree<int, Book>*)addr)->find(index).bookName.c_str());
}


extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_bookmanagerbybplustree_BookBPlusTree_findBookInfo(JNIEnv *env, jobject thiz,
                                                                   jlong addr, jint index) {
//    return toJstring(env, ((BPlusTree<int, Book>*)addr)->find(index).bookInfo);
    return env->NewStringUTF(((BPlusTree<int, Book>*)addr)->find(index).bookInfo.c_str());
}


extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_bookmanagerbybplustree_BookBPlusTree_findAuthor(JNIEnv *env, jobject thiz,
                                                                 jlong addr, jint index) {
    return env->NewStringUTF(((BPlusTree<int, Book>*)addr)->find(index).author.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_bookmanagerbybplustree_BookBPlusTree_findAuthorInfo(JNIEnv *env, jobject thiz,
                                                                     jlong addr, jint index) {
//    return toJstring(env, ((BPlusTree<int, Book>*)addr)->find(index).authorInfo);
    return env->NewStringUTF(((BPlusTree<int, Book>*)addr)->find(index).authorInfo.c_str());
}


extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_bookmanagerbybplustree_BookBPlusTree_findUrl(JNIEnv *env, jobject thiz, jlong addr,
                                                              jint index) {
//    return toJstring(env, ((BPlusTree<int, Book>*)addr)->find(index).url);
    return env->NewStringUTF(((BPlusTree<int, Book>*)addr)->find(index).url.c_str());
}
extern "C"
JNIEXPORT void JNICALL
Java_com_example_bookmanagerbybplustree_BookBPlusTree_eraseBook(JNIEnv *env, jobject thiz,
                                                                jlong addr, jint index) {
    ((BPlusTree<int, Book>*)addr)->erase(index);
}

/**
 * 借阅部分
 */
extern "C"
JNIEXPORT jlong JNICALL
Java_com_example_bookmanagerbybplustree_BorrowBPlusTree_createNativeObject(JNIEnv *env, jobject thiz) {
    return (jlong) new BPlusTree<int, Borrow>();
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_bookmanagerbybplustree_BorrowBPlusTree_insertBorrow(JNIEnv *env, jobject thiz,
                                                                     jlong addr, jint index,
                                                                     jint id, jstring user_name,
                                                                     jstring return_time,
                                                                     jstring book_name,
                                                                     jstring book_info,
                                                                     jstring author,
                                                                     jstring author_info,
                                                                     jstring url) {
    ((BPlusTree<int, Borrow>*)addr)->insert(index, {id,
                                                    JStrToStr(env, user_name),
                                                    JStrToStr(env, return_time),
                                                    JStrToStr(env, book_name),
                                                    JStrToStr(env, book_info),
                                                    JStrToStr(env, author),
                                                    JStrToStr(env, author_info),
                                                    JStrToStr(env, url)});

}


extern "C"
JNIEXPORT jint JNICALL
Java_com_example_bookmanagerbybplustree_BorrowBPlusTree_findId(JNIEnv *env, jobject thiz,
                                                               jlong addr, jint index) {
    return ((BPlusTree<int, Borrow>*)addr)->find(index).id;
}


extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_bookmanagerbybplustree_BorrowBPlusTree_findUserName(JNIEnv *env, jobject thiz,
                                                                     jlong addr, jint index) {
    return env->NewStringUTF(((BPlusTree<int, Borrow>*)addr)->find(index).userName.c_str());
}


extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_bookmanagerbybplustree_BorrowBPlusTree_findReturnTime(JNIEnv *env, jobject thiz,
                                                                       jlong addr, jint index) {
    return env->NewStringUTF(((BPlusTree<int, Borrow>*)addr)->find(index).returnTime.c_str());
}



extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_bookmanagerbybplustree_BorrowBPlusTree_findBookName(JNIEnv *env, jobject thiz,
                                                                     jlong addr, jint index) {
    return env->NewStringUTF(((BPlusTree<int, Borrow>*)addr)->find(index).bookName.c_str());
}


extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_bookmanagerbybplustree_BorrowBPlusTree_findBookInfo(JNIEnv *env, jobject thiz,
                                                                     jlong addr, jint index) {
    return env->NewStringUTF(((BPlusTree<int, Borrow>*)addr)->find(index).bookInfo.c_str());
}


extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_bookmanagerbybplustree_BorrowBPlusTree_findAuthor(JNIEnv *env, jobject thiz,
                                                                   jlong addr, jint index) {
    return env->NewStringUTF(((BPlusTree<int, Borrow>*)addr)->find(index).author.c_str());
}


extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_bookmanagerbybplustree_BorrowBPlusTree_findAuthorInfo(JNIEnv *env, jobject thiz,
                                                                       jlong addr, jint index) {
    return env->NewStringUTF(((BPlusTree<int, Borrow>*)addr)->find(index).authorInfo.c_str());
}


extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_bookmanagerbybplustree_BorrowBPlusTree_findUrl(JNIEnv *env, jobject thiz,
                                                                jlong addr, jint index) {
    return env->NewStringUTF(((BPlusTree<int, Borrow>*)addr)->find(index).url.c_str());
}


extern "C"
JNIEXPORT void JNICALL
Java_com_example_bookmanagerbybplustree_BorrowBPlusTree_eraseBorrow(JNIEnv *env, jobject thiz,
                                                                    jlong addr, jint index) {
    ((BPlusTree<int, Borrow>*)addr)->erase(index);
}

/**
 * 用户信息部分
 */
extern "C"
JNIEXPORT jlong JNICALL
Java_com_example_bookmanagerbybplustree_UserBPlusTree_createNativeObject(JNIEnv *env,
                                                                         jobject thiz) {
    return (jlong) new BPlusTree<int, User>();
}


extern "C"
JNIEXPORT void JNICALL
Java_com_example_bookmanagerbybplustree_UserBPlusTree_insertUser(JNIEnv *env, jobject thiz,
                                                                 jlong addr, jint id,
                                                                 jstring user_name, jstring pwd) {
    ((BPlusTree<int, User>*)addr)->insert(id, {id,
                                                    JStrToStr(env, user_name),
                                                    JStrToStr(env, pwd)});
}



extern "C"
JNIEXPORT jint JNICALL
Java_com_example_bookmanagerbybplustree_UserBPlusTree_findId(JNIEnv *env, jobject thiz, jlong addr,
                                                             jint index) {
    return ((BPlusTree<int, User>*)addr)->find(index).id;
}


extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_bookmanagerbybplustree_UserBPlusTree_findUserName(JNIEnv *env, jobject thiz,
                                                                   jlong addr, jint index) {
    return env->NewStringUTF(((BPlusTree<int, User>*)addr)->find(index).userName.c_str());
}


extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_bookmanagerbybplustree_UserBPlusTree_findPwd(JNIEnv *env, jobject thiz, jlong addr,
                                                              jint index) {
    return env->NewStringUTF(((BPlusTree<int, User>*)addr)->find(index).pwd.c_str());
}