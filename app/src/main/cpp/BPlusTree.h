//
// Created by A1029 on 2022/4/2.
//

#ifndef CPPJNITEST_BPLUSTREE_H
#define CPPJNITEST_BPLUSTREE_H

#include <iostream>
#include <cstdio>
#include <cstdlib>
#include <queue>
#include <cstring>
#include <jni.h>
using namespace std;

#define M 3         // 阶数
#define m (M / 2)   // 阶数向下取整

// 存放用户信息
struct User {
    int id;
    string userName, pwd;
};

// 存放借阅信息
struct Borrow {
    int id;
    string userName, returnTime, bookName, bookInfo, author, authorInfo, url;
};

// 存放图书信息
struct Book {
    int id, available, total;
    string bookName, bookInfo, author, authorInfo, url;
};

template<class K, class E>
struct treeNode {
    K k[M];                         // 当前层的索引区间
    E* e[M];                         // 叶子节点数据区
    treeNode<K, E>* p[M + 1];       // 指向下一层的节点
    treeNode<K, E>* parent;         // 父节点
    int cnt;                        // 记录节点个数
    bool isLeaf;                    // 是否为叶子节点
    treeNode<K, E> *prev, *next;    // B+树数据区的前一个节点和后一个节点
};

template<class K, class E>
class BPlusTree {

public:
    BPlusTree();    // 构造函数
    // ~BPlusTree();   // 析构函数
    virtual void insert(K index, E target);    // 插入
    virtual void erase(K index);    // 删除
    virtual E find(K index) const;  // 由关键字搜索
    E* searchAll() const;  // 显示B+树中前20个节点

private:
    virtual treeNode<K, E>* nodeCreate();   // 创建新的B+树节点
    virtual void nodeSplit(treeNode<K, E>* child);    // 分裂节点
    virtual void nodeMerge(treeNode<K, E>* node); // 归并非叶子节点
    virtual treeNode<K, E>* search(K index) const;    // 搜索可插入索引为index的叶节点所在位置
    virtual void display() const;      // 显示B+树结构
    virtual void displayLeaf() const;   // 显示全部叶子节点元素

    treeNode<K, E>* root;    // B+树根节点
    int nodeCnt;        // B+树中节点数量
};

// 构造函数
template<class K, class E>
BPlusTree<K, E>::BPlusTree() {
    // root = TreeCreate();

    root = nodeCreate();
    // root->next = root->prev = root;  // 将其前后指针均指向自己

    nodeCnt = 0;
}

// 创建新的B+树节点
template<class K, class E>
treeNode<K, E>* BPlusTree<K, E>::nodeCreate() {
    treeNode<K, E>* node = new treeNode<K, E>;

    for (int i = 0; i < M; i++) {   // 创建B+树节点
        node->k[i] = 0;
        node->e[i] = NULL;
    }

    for (int i = 0; i < M + 1; i++) // 创建指向下一层的指针
        node->p[i] = NULL;

    node->cnt = 0;
    node->isLeaf = true;
    node->prev = node->next = NULL;
    node->parent = NULL;

    return node;
}


// 搜索可插入索引为index的叶节点所在位置
template<class K, class E>
treeNode<K, E>* BPlusTree<K, E>::search(K index) const {
    treeNode<K, E>* node = root;
    while (!node->isLeaf) {   // 当前节点的子节点非叶子节点时
        int i = 0;
        while (i < node->cnt && node->k[i] <= index) i++;     // 找到合适的位置
        node = node->p[i];
    }
    return node;
}

// 由关键字搜索元素
template<class K, class E>
E BPlusTree<K, E>::find(K index) const {
    treeNode<K, E>* node = root;
    while (!node->isLeaf) {
        int i = 0;
        while (i < node->cnt && node->k[i] <= index) i++;
        node = node->p[i];
    }
    int i = 0;
    while (node->k[i] < index && i < node->cnt - 1) i++;
    return *node->e[i];
}

// 显示B+树中前20个节点
template<class K, class E>
E* BPlusTree<K, E>::searchAll() const {
    treeNode<K, E>* node = root;
    while (!node->isLeaf)
        node = node->p[0];

    E* tmp = new E[20];
    int cnt = 0;
    for (int i = 0; i < 20 && i < nodeCnt; i++) {
        tmp[i] = *node->e[cnt++];
        if (cnt >= node->cnt) {
            node = node->next;
            cnt = 0;
        }
    }
    return tmp;
}


// 分裂节点
template<class K, class E>
void BPlusTree<K, E>::nodeSplit(treeNode<K, E>* child) {

    // int pos = (M % 2 == 0) ? (M / 2 - 1) : M / 2;    // 分裂位置
    treeNode<K, E>* rightChild = nodeCreate();    // 新分裂出的右孩子

    if (child->isLeaf) {    // child为叶子节点时

        rightChild->isLeaf = child->isLeaf;
        rightChild->cnt = M - m;
        child->cnt = m;

        for (int i = 0; i < rightChild->cnt; i++) { // 将原节点的后M-m个节点放到rightChild前面
            rightChild->k[i] = child->k[i + m];
            rightChild->e[i] = child->e[i + m];
        }

        if (child->parent != NULL) {    // 根节点非叶子节点时
            // for (int i = 0; i < child->parent->cnt; i++) {
            //     cout << child->parent->k[i] << " ";
            // }
            // cout << endl;

            treeNode<K, E>* parent = child->parent;

            int pos = parent->cnt;
            // for (i = parent->cnt; i > pos; i--) {
            while (pos >= 1 && parent->k[pos - 1] > rightChild->k[0]) { // 移动parent孩子节点位置、改变索引
                parent->k[pos] = parent->k[pos - 1];
                parent->p[pos + 1] = parent->p[pos];
                pos--;
            }
            parent->p[pos + 1] = rightChild;        // parent新增节点右侧指针指向rightChild
            parent->k[pos] = rightChild->k[0];      // parent新增节点索引为rightChild中最小的索引
            // parent->p[pos] = child;
            parent->cnt++;

            rightChild->next = child->next;
            if (child->next != NULL)
                child->next->prev = rightChild;
            rightChild->prev = child;
            child->next = rightChild;

            rightChild->parent = parent;

            if (parent->cnt == M)   // 若parent已满，从下向上递归分裂
                nodeSplit(parent);

        } else {    // 根节点为叶子节点
            treeNode<K, E>* parent = nodeCreate();

            parent->k[0] = rightChild->k[0];
            parent->isLeaf = false;
            parent->cnt = 1;
            parent->p[0] = child;
            parent->p[1] = rightChild;

            rightChild->next = child->next;
            if (child->next != NULL)
                child->next->prev = rightChild;
            rightChild->prev = child;
            child->next = rightChild;

            child->parent = parent;
            rightChild->parent = parent;

            root = parent;
        }

    } else {    // 不为叶子节点时

        rightChild->isLeaf = child->isLeaf;
        // rightChild->cnt = M - m - 1;
        // child->cnt = (M % 2 == 0) ? m - 1 : m;
        child->cnt = m;
        rightChild->cnt = M - m - 1;

        for (int i = 0; i < rightChild->cnt; i++)   // 修改新节点索引
            rightChild->k[i] = child->k[i + m + 1];

        int tmp = m + 2 - 1; // 修改新节点指针
        for (int i = 0; i <= rightChild->cnt; i++) {
            rightChild->p[i] = child->p[tmp + i];
            rightChild->p[i]->parent = rightChild;
        }

        if (child->parent != NULL) {    // 不为根节点时

            treeNode<K, E>* parent = child->parent;

            int pos = parent->cnt;
            while (pos >= 1 && parent->k[pos - 1] > rightChild->k[0]) {   // 移动parent孩子节点位置、改变索引
                parent->k[pos] = parent->k[pos - 1];
                parent->p[pos + 1] = parent->p[pos];
                pos--;
            }
            parent->p[pos + 1] = rightChild;        // parent新增节点右侧指针指向rightChild
            parent->k[pos] = child->k[child->cnt];      // parent新增节点索引为现在child有效索引的后一个
            // parent->p[pos] = child;
            parent->cnt++;

            child->parent = parent;
            rightChild->parent = parent;

            if (parent->cnt == M)   // 若parent已满，从下向上递归分裂
                nodeSplit(parent);

        } else {    // 为根节点时

            treeNode<K, E>* parent = nodeCreate();

            parent->k[0] = child->k[child->cnt];    // parent新增节点索引为现在child有效索引的后一个
            parent->isLeaf = false;
            parent->cnt = 1;
            parent->p[0] = child;
            parent->p[1] = rightChild;

            child->parent = parent;
            rightChild->parent = parent;

            root = parent;
        }
    }
}

// 在各级上插入新节点
template<class K, class E>
void BPlusTree<K, E>::insert(K index, E target) {

    // if (node->isLeaf) { // 插入位置为叶子节点

    treeNode<K, E>* node = search(index);   // 查找将要插入的叶子节点

    for (int i = 0; i < node->cnt; i++) {   // 遍历该节点，查看是否已存在关键字为index的元素
        if (node->k[i] == index) {  // 存在则替换
            // node->e[i] = new E(target);
            delete node->e[i];
            node->e[i] = new E(target);
            return;
        }
    }

    int pos = node->cnt;        // 插入位置
    while (pos >= 1 && index < node->k[pos - 1]) { // 将比插入元素索引大的节点向后移动
        node->k[pos] = node->k[pos - 1];
        node->e[pos] = node->e[pos - 1];
        pos--;
    }

    node->k[pos] = index;
    node->e[pos] = new E(target);
    node->cnt++;
    nodeCnt++;

    if (node->cnt == M)                             // 若当前节点已满，进行分裂
        nodeSplit(node);

}

// 删除
template<class K, class E>
void BPlusTree<K, E>::erase(K index) {

    treeNode<K, E>* node = search(index);   // 找到节点所在位置

    int pos;
    for (pos = 0; pos < node->cnt; pos++) { // 查找目标节点是否存在
        if (node->k[pos] == index) break;
    }
    if (pos == node->cnt) return;   // 不存在，无需删除

    if (node == root && node->cnt == 1) {     // 考虑删除根节点上唯一元素的情况
        root = nodeCreate();
        nodeCnt = 0;

        delete node;
        return;
    }

    if (node->cnt != 1) {   // 无需归并节点，可直接删除

        delete node->e[pos];
        node->e[pos] = NULL;

        for (int i = pos; i < node->cnt - 1; i++) { // 将删除位置之后的节点向前移动
            node->k[i] = node->k[i + 1];
            node->e[i] = node->e[i + 1];
        }
        node->e[node->cnt - 1] = NULL;
        node->cnt--;

        if (pos == 0) { // 若删除位置为叶子节点中的第一个元素，更新上层索引
            bool changed = false;
            treeNode<K, E>* tmp = node;
            while (!changed && tmp != root) {
                tmp = tmp->parent;
                for (int i = 0; i < tmp->cnt; i++) {
                    if (tmp->k[i] == index) {
                        tmp->k[i] = node->k[0];
                        changed = true;
                        break;
                    }
                }
            }
        }

    } else {    // 需进行元素转移或节点归并
        treeNode<K, E>* parent = node->parent;

        int pos;
        for (pos = 0; pos < parent->cnt + 1; pos++) {
            if (parent->p[pos] == node)
                break;
        }

        if (pos != 0 && parent->p[pos - 1]->cnt != 1) { // 从左侧叶子节点转移元素
            treeNode<K, E>* leftNode = parent->p[pos - 1];

            delete node->e[0];
            node->k[0] = leftNode->k[leftNode->cnt - 1];
            node->e[0] = leftNode->e[leftNode->cnt - 1];
            leftNode->e[leftNode->cnt] = NULL;
            leftNode->cnt--;

            parent->k[pos - 1] = node->k[0];

        } else if (pos < parent->cnt && parent->p[pos + 1]->cnt != 1) {    // 从右侧叶子节点转移元素

            treeNode<K, E>* rightNode = parent->p[pos + 1];

            delete node->e[0];
            node->k[0] = rightNode->k[0];
            node->e[0] = rightNode->e[0];

            for (int i = 0; i < rightNode->cnt - 1; i++) {
                rightNode->k[i] = rightNode->k[i + 1];
                rightNode->e[i] = rightNode->e[i + 1];
            }
            rightNode->e[rightNode->cnt] = NULL;
            rightNode->cnt--;

            parent->k[pos] = rightNode->k[0];

            bool changed = false;   // 更新上层索引
            treeNode<K, E>* tmp = node;
            while (!changed && tmp != root) {
                tmp = tmp->parent;
                for (int i = 0; i < tmp->cnt; i++) {
                    if (tmp->k[i] == index) {
                        tmp->k[i] = node->k[0];
                        changed = true;
                        break;
                    }
                }
            }

        } else if (parent->cnt != 1) {    // 在父节点中进行位置移动

            if (pos == 0) { // 若删除位置为叶子节点中的第一个元素，更新上层索引
                bool changed = false;
                treeNode<K, E>* tmp = node;
                while (!changed && tmp != root) {
                    tmp = tmp->parent;
                    for (int i = 0; i < tmp->cnt; i++) {
                        if (tmp->k[i] == index) {
                            tmp->k[i] = parent->p[1]->k[0];
                            changed = true;
                            break;
                        }
                    }
                }
            }

            if (node->prev != NULL)     // 改变叶子结点指针
                node->prev->next = node->next;
            if (node->next != NULL)
                node->next->prev = node->prev;

            delete node->e[0];  // 删除目标节点
            delete node;
            parent->p[pos] = NULL;

            for (int i = pos; i < parent->cnt - 1; i++) {   // 移动父节点指针
                parent->k[i] = parent->k[i + 1];
                parent->p[i] = parent->p[i + 1];
            }
            parent->p[parent->cnt - 1] = parent->p[parent->cnt];
            parent->cnt--;

        } else {    // 需要归并父节点

            if (pos == 0 && node->prev != NULL) { // 若删除位置为叶子节点中的第一个元素，更新上层索引，若删除第二个节点，含有该节点索引的父节点稍后会被归并，无需更新索引
                bool changed = false;
                treeNode<K, E>* tmp = node;
                while (!changed && tmp != root) {
                    tmp = tmp->parent;
                    for (int i = 0; i < tmp->cnt; i++) {
                        if (tmp->k[i] == index) {
                            tmp->k[i] = parent->p[1]->k[0];
                            changed = true;
                            break;
                        }
                    }
                }
            }

            if (node->prev != NULL)     // 改变叶子结点指针
                node->prev->next = node->next;
            if (node->next != NULL)
                node->next->prev = node->prev;

            delete node->e[0];  // 删除目标节点
            delete node;

            parent->cnt = 0;

            if (pos == 0)   // 将唯一的子节点转移到第一位
                parent->p[0] = parent->p[1];

            nodeMerge(parent);    // 对非叶子节点进行归并
        }

    }

    nodeCnt--;
}

// 归并非叶子节点
template<class K, class E>
void BPlusTree<K, E>::nodeMerge(treeNode<K, E>* node) {

    if (node == root) {
        root = node->p[0];
        root->parent = NULL;

        delete node;
        return;
    }

    treeNode<K, E>* parent = node->parent;  // 需归并节点的父节点
    treeNode<K, E>* tempNode = NULL;    // 合并关键字的节点

    int pos;    // 寻找父节点中指向node的指针
    for (pos = 0; pos < parent->cnt + 1; pos++)
        if (parent->p[pos] == node)
            break;

    if (pos != 0) {         // 与父节点中前一个指针指向的节点进行归并

        if ((parent->p[pos - 1]->cnt == 1 && pos == parent->cnt) ||
            (parent->p[pos - 1]->cnt == 1 && pos < parent->cnt && parent->p[pos + 1]->cnt == 1)) {   // 进行节点归并
            tempNode = parent->p[pos - 1];

            tempNode->k[tempNode->cnt] = parent->k[pos - 1];    // 将被归并节点转移到tempNode尾部
            tempNode->p[tempNode->cnt + 1] = node->p[0];
            node->p[0]->parent = tempNode;
            tempNode->cnt++;

            for (int i = pos; i < parent->cnt; i++) {   // 移动父节点指针
                parent->k[pos - 1] = parent->k[pos];
                parent->p[pos] = parent->p[pos + 1];
            }
            parent->cnt--;

            delete node;

        } else if (parent->p[pos - 1]->cnt > 1) {  // 可从左孩子中转移节点

            tempNode = parent->p[pos - 1];

            node->k[0] = parent->k[pos - 1];
            node->p[1] = node->p[0];
            node->cnt++;

            node->p[0] = tempNode->p[tempNode->cnt];
            node->p[0]->parent = node;
            parent->k[pos - 1] = tempNode->k[tempNode->cnt - 1];
            tempNode->p[tempNode->cnt] = NULL;

            tempNode->cnt--;

        } else {        // 从右孩子中转移节点

            tempNode = parent->p[pos + 1];

            node->k[0] = parent->k[pos];
            node->p[1] = tempNode->p[0];
            tempNode->p[0]->parent = node;
            node->cnt++;

            parent->k[pos] = tempNode->k[0];
            for (int i = 0; i < tempNode->cnt - 1; i++) {
                tempNode->k[i] = tempNode->k[i + 1];
                tempNode->p[i] = tempNode->p[i + 1];
            }
            tempNode->p[tempNode->cnt - 1] = tempNode->p[tempNode->cnt];
            tempNode->cnt--;
        }

    } else {            // 与父节点中后一个指针指向的节点进行归并

        if (parent->p[1]->cnt == 1) {

            tempNode = parent->p[pos + 1];

            tempNode->p[tempNode->cnt + 1] = tempNode->p[tempNode->cnt];    // 将tempNode节点元素向后移动
            for (int i = tempNode->cnt; i > 0; i--) {
                tempNode->k[i] = tempNode->k[i - 1];
                tempNode->p[i] = tempNode->p[i - 1];
            }
            tempNode->k[0] = parent->k[0];
            tempNode->p[0] = node->p[0];
            node->p[0]->parent = tempNode;

            tempNode->cnt++;

            for (int i = 0; i < parent->cnt - 1; i++) {     // 移动父节点指针
                parent->k[i] = parent->k[i + 1];
                parent->p[i] = parent->p[i + 1];
            }
            parent->p[parent->cnt - 1] = parent->p[parent->cnt];

            parent->cnt--;

            delete node;

        } else {    // 从右侧兄弟节点中转移节点
            tempNode = parent->p[pos + 1];

            node->k[0] = parent->k[pos];
            node->p[1] = tempNode->p[0];
            tempNode->p[0]->parent = node;
            node->cnt++;

            parent->k[pos] = tempNode->k[0];
            for (int i = 0; i < tempNode->cnt - 1; i++) {
                tempNode->k[i] = tempNode->k[i + 1];
                tempNode->p[i] = tempNode->p[i + 1];
            }
            tempNode->p[tempNode->cnt - 1] = tempNode->p[tempNode->cnt];
            tempNode->cnt--;

        }
    }

    if (parent == root && parent->cnt == 0) {   // 更新根节点
        root = parent->p[0];
        delete parent;

        return;
    }

    if (parent->cnt == 0)
        nodeMerge(parent);

}


// 显示B+树结构
template<class K, class E>
void BPlusTree<K, E>::display() const {
    treeNode<K, E>* node;
    std::queue<treeNode<K, E>* > q;
    q.push(root);
    while (!q.empty()) {
        node = q.front();
        q.pop();
        if (node != NULL) {
            printf("%d----- ", node->cnt);
            for (int i = 0; i < node->cnt; i++)
                printf("%d ", node->k[i]);
            printf("|| ");
            for (int i = 0; i <= node->cnt; i++)
                q.push(node->p[i]);
            if (!node->isLeaf)
                q.push(NULL);
        } else {
            printf("\n");
        }
    }
}

// 显示全部叶子节点元素
template<class K, class E>
void BPlusTree<K, E>::displayLeaf() const {

    treeNode<K, E>* node = root;
    while (!node->isLeaf) {
        node = node->p[0];
    }

    while (node != NULL) {
        for (int i = 0; i < node->cnt; i++)
            printf("%d ", node->k[i]);
        printf("\n");
        node = node->next;
    }
}

#endif
